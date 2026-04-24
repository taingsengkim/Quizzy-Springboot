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
        @Size(min = 8, max = 150)
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
                message = "Password must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 special character"
        )
        String password
) {
}
