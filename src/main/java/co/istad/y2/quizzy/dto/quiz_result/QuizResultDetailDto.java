package co.istad.y2.quizzy.dto.quiz_result;

import java.util.List;

public record QuizResultDetailDto(
        Long resultId,
        String quizTitle,
        Integer score,
        Integer total,
        List<QuestionResultDto> questions
) {
}
