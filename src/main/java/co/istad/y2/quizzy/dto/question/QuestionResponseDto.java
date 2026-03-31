package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.AnswerResponseDto;
import co.istad.y2.quizzy.model.Difficulty;
import co.istad.y2.quizzy.model.QuestionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public record QuestionResponseDto(
        Long id,
        String text,
        Long quizId,
        List<AnswerResponseDto> answers,
        QuestionType questionType,
        Integer points,

        Difficulty difficulty
) {
}
