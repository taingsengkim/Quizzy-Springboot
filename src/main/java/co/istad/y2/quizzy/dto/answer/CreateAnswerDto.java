package co.istad.y2.quizzy.dto.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAnswerDto(
        @NotBlank
        String text,
        Boolean correct
) {
}
