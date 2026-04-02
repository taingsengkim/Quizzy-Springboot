package co.istad.y2.quizzy.dto.quiz_result;

import java.util.List;

public record QuestionResultDto(
        Long questionId,
        String questionText,
        List<String> correctAnswers,
        List<String> userAnswers
) {
}
