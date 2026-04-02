package co.istad.y2.quizzy.dto.quiz;

import co.istad.y2.quizzy.dto.answer.CreateAnswerDto;
import co.istad.y2.quizzy.model.QuestionType;
import jakarta.validation.constraints.*;

import java.util.List;

public record QuestionCreateDto(
        @NotBlank(message = "Question text is required")
        @Size(max = 500, message = "Question text must be less than 500 characters")
        String text,

        @NotNull(message = "Question type is required")
        QuestionType questionType,

        @NotNull(message = "Points are required")
        @Positive(message = "Points must be a positive number")
        Integer points,

        @NotBlank(message = "Difficulty is required")
        String difficulty,

        @NotEmpty(message = "Answers cannot be empty")
        List<@NotNull CreateAnswerDto> answers
) {
}
