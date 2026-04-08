package co.istad.y2.quizzy.dto.auth;


public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {}