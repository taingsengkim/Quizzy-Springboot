package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.dto.answer.UpdateAnswerDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateQuestionDto(
        @NotBlank
        @Size(max = 500)
        String text,
        Long categoryId,
        List<UpdateAnswerDto> answers
) {
}
