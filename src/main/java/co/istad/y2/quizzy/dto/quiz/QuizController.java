package co.istad.y2.quizzy.dto.quiz;


import java.util.List;

public record QuizController(
         String title,      // Quiz title
         String description,            // Quiz description
         Integer duration,              // Duration in minutes
         Long categoryId,
         List<QuestionCreateDto> questions
) {
}
