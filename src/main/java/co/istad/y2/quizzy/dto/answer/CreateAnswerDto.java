package co.istad.y2.quizzy.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAnswerDto(
        @NotBlank(message = "Answer text cannot be empty")
        @Size(max = 500, message = "Answer text must be at most 500 characters")        String text,
        Boolean correct
) {
}
