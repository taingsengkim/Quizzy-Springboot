package co.istad.y2.quizzy.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto (
        @NotBlank
        @Size(max = 150)
        String username,
        @NotBlank
        @Size(max = 150)
        String email,
        @NotBlank
        @Size(max = 150)
        String password
){
}
