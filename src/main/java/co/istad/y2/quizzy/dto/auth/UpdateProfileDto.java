package co.istad.y2.quizzy.dto.auth;

public record UpdateProfileDto (
        String username,
        String oldPassword,
        String password,
        String avatar
){
}
