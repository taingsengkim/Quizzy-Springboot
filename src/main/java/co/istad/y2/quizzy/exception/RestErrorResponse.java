package co.istad.y2.quizzy.exception;


import lombok.Builder;

import java.time.Instant;

@Builder
public record RestErrorResponse(
    String message,
    Integer code,
    String status,
    Instant timestamp,
    Object errorDetails
) {
}
