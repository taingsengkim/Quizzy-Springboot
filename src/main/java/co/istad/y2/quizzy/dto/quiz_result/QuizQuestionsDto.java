package co.istad.y2.quizzy.dto.quiz_result;

import java.util.List;

public record QuizQuestionsDto (
        Long id,
        String text,
        List<QuizAnswerDto> answers
){
}
