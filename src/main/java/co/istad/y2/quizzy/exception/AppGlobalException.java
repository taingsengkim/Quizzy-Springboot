package co.istad.y2.quizzy.exception;


import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class AppGlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestErrorResponse handleValidation(MethodArgumentNotValidException e){
        List<FieldResponse> fields = new ArrayList<>();
        e.getFieldErrors().forEach(fieldError -> {
            fields.add(new FieldResponse(fieldError.getField(),fieldError.getDefaultMessage()));
        });
        return buildError(HttpStatus.BAD_REQUEST,"Requested data is invalid", fields);
    }
    private RestErrorResponse buildError(HttpStatus status, String message, Object details) {
        return RestErrorResponse.builder()
                .message(message)
                .code(status.value())
                .status(status.getReasonPhrase())
                .timestamp(Instant.now())
                .errorDetails(details)
                .build();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public RestErrorResponse handleResponseStatusException(MissingRequestHeaderException e) {
        return buildError(
                HttpStatus.valueOf(e.getStatusCode().value()),
                e.getMessage(),
                null
        );
    }
    @ExceptionHandler(ResponseStatusException.class)
    public RestErrorResponse handleResponseStatusException(ResponseStatusException e) {

        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());

        return buildError(
                status,
                e.getReason() != null ? e.getReason() : "Unexpected error",
                null
        );
    }
}
