package co.istad.y2.quizzy.dto.answer;

public record AnswerResponseDto(
        Long id,
        String text,
        Boolean correct
) {
}
