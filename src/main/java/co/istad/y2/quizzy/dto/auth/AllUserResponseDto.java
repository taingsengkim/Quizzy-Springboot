package co.istad.y2.quizzy.dto.auth;

public record AllUserResponseDto(
        Long id,
        String username,
        String email,
        String avatar
){
}
