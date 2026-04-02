package co.istad.y2.quizzy.dto.quiz;

public record QuizUpdateDto(
        String title,
        String description,
        Integer duration,
        Long categoryId
) {
}
