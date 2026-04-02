package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.dto.answer.UpdateAnswerDto;
import co.istad.y2.quizzy.model.Difficulty;
import co.istad.y2.quizzy.model.QuestionType;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateQuestionDto(
        @NotBlank(message = "Question text is required")
        @Size(max = 500, message = "Question text must be less than 500 characters")
        String text,

        @NotNull(message = "Quiz ID is required")
        Long quizId,

        @NotNull(message = "Question type is required")
        QuestionType questionType,

        @NotNull(message = "Points are required")
        @Positive(message = "Points must be a positive number")
        Integer points,

        @NotNull(message = "Difficulty is required")
        Difficulty difficulty,

        @NotEmpty(message = "Answers cannot be empty")
        List<@NotNull UpdateAnswerDto> answers
) {
}
