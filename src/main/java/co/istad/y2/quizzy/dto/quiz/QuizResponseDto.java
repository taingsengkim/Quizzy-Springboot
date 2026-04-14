package co.istad.y2.quizzy.dto.quiz;

import co.istad.y2.quizzy.dto.question.QuestionResponseDto;

import java.util.List;

public record QuizResponseDto(
        Long id,
        String title,
        String description,
        Integer duration,
//        String categoryName,
        Long categoryId,

//        String createdByUsername,
        List<QuestionResponseDto> questions
) {
}
