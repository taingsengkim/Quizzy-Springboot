package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.dto.answer.UpdateAnswerDto;
import co.istad.y2.quizzy.model.Difficulty;
import co.istad.y2.quizzy.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateQuestionDto(
        @NotBlank
        @Size(max = 500)
        String text,
        Long quizId,
        QuestionType questionType,
        Integer points,
        Difficulty difficulty,
        List<UpdateAnswerDto> answers
) {
}
