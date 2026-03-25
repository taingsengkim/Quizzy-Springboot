package co.istad.y2.quizzy.exception;


import co.istad.y2.quizzy.exception.question.QuestionNotFoundException;
import co.istad.y2.quizzy.exception.user.EmailAlreadyExist;
import co.istad.y2.quizzy.exception.user.InvalidCredentialsException;
import co.istad.y2.quizzy.exception.user.UserNotFound;
import co.istad.y2.quizzy.exception.user.UsernameAlreadyExist;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        return buildError(HttpStatus.BAD_REQUEST,"Requested data is invalid", e.getMessage());
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EmailAlreadyExist.class,
            UsernameAlreadyExist.class,
            UserNotFound.class,
            QuestionNotFoundException.class
    })
    public RestErrorResponse handleEmailDuplicate(Exception e){
       return buildError(HttpStatus.BAD_REQUEST,"Invalid Data Request", e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public RestErrorResponse handleInvalidCredentials(InvalidCredentialsException e){
        return buildError(HttpStatus.BAD_REQUEST,"Authentication Failed", e.getMessage());
    }

}
