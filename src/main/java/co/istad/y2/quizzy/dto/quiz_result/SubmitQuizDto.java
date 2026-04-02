package co.istad.y2.quizzy.dto.quiz_result;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitQuizDto (
        @NotNull(message = "Quiz ID is required")
        Long quizId,               // the quiz being submitted
        Long duration,             // time in seconds or milliseconds
        @NotEmpty(message = "Answers must not be empty")
        List<SubmitAnswerDto> answers
) {}