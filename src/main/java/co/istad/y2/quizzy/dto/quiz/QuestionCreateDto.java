package co.istad.y2.quizzy.dto.quiz;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.model.QuestionType;

import java.util.List;

public record QuestionCreateDto(
        String text,
        QuestionType questionType,
        Integer points,
        String difficulty,
        List<CreateAnswerDto> answers
) {
}
