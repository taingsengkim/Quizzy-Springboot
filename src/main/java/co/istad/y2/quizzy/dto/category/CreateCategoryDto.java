package co.istad.y2.quizzy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryDto(
   @NotBlank
   @Size(max = 150)
    String name
) {
}
