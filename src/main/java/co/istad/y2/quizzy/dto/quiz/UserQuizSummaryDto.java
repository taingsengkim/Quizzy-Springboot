package co.istad.y2.quizzy.dto.quiz;

public record UserQuizSummaryDto(
        Long quizId,
        String quizTitle,
        Integer totalQuestions,
        Integer correctAnswers,
        Long duration
) {}