package co.istad.y2.quizzy.dto.quiz;

import co.istad.y2.quizzy.model.Answer;

import java.util.List;

public record QuizQuestionsDto (
        Long id,
        String text,
        List<QuizAnswerDto> answers
){
}
