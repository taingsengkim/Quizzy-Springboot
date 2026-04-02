package co.istad.y2.quizzy.dto.quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuizCreateDto(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,
        @Size(max = 1000, message = "Description must be less than 1000 characters")
        String description,
        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be a positive number")
        Integer duration,
        @NotNull(message = "Category ID is required")
        Long categoryId,
        @NotNull(message = "Category ID is required")
        List<QuestionCreateDto> questions
) {
}
