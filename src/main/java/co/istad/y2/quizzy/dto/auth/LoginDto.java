package co.istad.y2.quizzy.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginDto(
        @NotBlank
        @Size(max = 150)
        @Email(message = "Email should be valid")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a Gmail address")
        String email,

        @NotBlank
        @Size(max = 150)
        String password


) {
}
