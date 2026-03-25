package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateQuestionDto(
        @NotBlank
        @Size(max = 500)
        String text,
        @NotBlank
        Long categoryId,
        List<CreateAnswerDto> answers
) {
}
