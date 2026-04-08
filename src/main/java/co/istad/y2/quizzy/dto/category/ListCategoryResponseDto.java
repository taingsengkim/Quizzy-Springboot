package co.istad.y2.quizzy.dto.category;

public record ListCategoryResponseDto(
        Long id,
        String name,
        int totalQuiz,
        String description,
        String imageUrl
) {
}
