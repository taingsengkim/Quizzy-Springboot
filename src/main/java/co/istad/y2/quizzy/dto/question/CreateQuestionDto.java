package co.istad.y2.quizzy.dto.question;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.model.Difficulty;
import co.istad.y2.quizzy.model.QuestionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateQuestionDto(
        @NotBlank(message = "Question text is required")
        @Size(max = 500, message = "Question text must be less than 500 characters")
        String text,

        String code,

        @NotNull(message = "Quiz ID is required")
        Long quizId,
        @Size(max = 1000, message = "Hint must be less than 1000 characters")
        String hint,

        @NotNull(message = "Question type is required")
        @Enumerated(EnumType.STRING)
        QuestionType questionType,

        @NotNull(message = "Points are required")
        @Positive(message = "Points must be a positive number")
        Integer points,

        @NotNull(message = "Difficulty is required")
        @Enumerated(EnumType.STRING)
        Difficulty difficulty,

        @NotEmpty(message = "Answers cannot be empty")
        List<@NotNull CreateAnswerDto> answers
) {
}
