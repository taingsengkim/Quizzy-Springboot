package co.istad.y2.quizzy.dto.quiz_result;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitAnswerDto (
        @NotNull(message = "Question ID is required")
        Long questionId,
        @NotEmpty(message = "Answer ID must not be empty")
        List<Long> answerId
){
}
