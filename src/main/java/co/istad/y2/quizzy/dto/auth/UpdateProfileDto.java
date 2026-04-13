package co.istad.y2.quizzy.dto.auth;

public record UpdateProfileDto (
        String username,
        String password,
        String avatar
){
}
