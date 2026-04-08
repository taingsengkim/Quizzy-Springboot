package co.istad.y2.quizzy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryDto (
        @NotBlank(message = "Category name is required")
        @Size(max = 150, message = "Category name must be at most 150 characters")
        String name,
        @Size(max = 250,message = "Description must be at most 259 characters")
        String description,
        @Size(max = 220)
        String imageUrl
){
}
