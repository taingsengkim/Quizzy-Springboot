package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.model.Difficulty;
import co.istad.y2.quizzy.model.QuestionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateQuestionDto(
        @NotBlank
        @Size(max = 500)
        String text,
        @NotBlank
        Long quizId,

        @Enumerated(EnumType.STRING)
        QuestionType questionType,
        Integer points,

        @Enumerated(EnumType.STRING)
        Difficulty difficulty,
        List<CreateAnswerDto> answers
) {
}
