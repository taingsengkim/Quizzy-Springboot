package co.istad.y2.quizzy.dto.quiz_result;

public record QuizResultHistoryDto(
        Long resultId,
        String quizTitle,
        Integer score,
        Integer total,
        Long duration
) {
}
