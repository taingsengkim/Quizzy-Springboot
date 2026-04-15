package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.AnswerPlayDto;

import java.util.List;

public record QuestionPlayDto(
        Long id,
        String text,
        List<AnswerPlayDto> answers,
        String questionType,
        Integer points,
        String code,
        String difficulty,
        String hint
) {
}
