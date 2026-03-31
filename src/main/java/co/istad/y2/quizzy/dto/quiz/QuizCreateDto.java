package co.istad.y2.quizzy.dto.quiz;

import java.util.List;

public record QuizCreateDto(
        String title,
        String description,
        Integer duration,
        Long categoryId,
        List<QuestionCreateDto> questions
) {
}
