package co.istad.y2.quizzy.dto.quiz_result;

public record QuizResultResponseDto (
        int score,
        int total,
        Long resultId,
        Long duration
){
}
