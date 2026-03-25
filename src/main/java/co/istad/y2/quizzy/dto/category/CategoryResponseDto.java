package co.istad.y2.quizzy.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryResponseDto(
        Long id,
        String name
) {
}
