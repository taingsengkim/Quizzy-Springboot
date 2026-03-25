package co.istad.y2.quizzy.dto.auth;


import lombok.Builder;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String avatar
) {
}
