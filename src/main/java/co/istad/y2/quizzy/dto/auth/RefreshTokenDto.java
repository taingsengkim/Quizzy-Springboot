package co.istad.y2.quizzy.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto (
        @NotBlank(message = "Refresh token is required")
        String refreshToken
){

}
