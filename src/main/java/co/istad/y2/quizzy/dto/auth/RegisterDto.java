package co.istad.y2.quizzy.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDto (
        @NotBlank
        @Size(max = 150)
        String username,

        @NotBlank
        @Size(max = 150)
        @Email(message = "Email should be valid")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a Gmail address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 150, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
                message = "Password must contain uppercase, lowercase, number, special character and no spaces"
        )
        String password,

        @NotBlank(message = "Role Is Required")
        @Size(max = 20)
        String role
){
}
