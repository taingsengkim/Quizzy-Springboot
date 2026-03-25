package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.AnswerResponseDto;

import java.util.List;

public record QuestionResponseDto(
        Long id,
        String text,
        String categoryName,
        String createdBy,
        List<AnswerResponseDto> answers
) {
}
