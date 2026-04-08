package co.istad.y2.quizzy.dto.quiz;

import co.istad.y2.quizzy.dto.question.QuestionPlayDto;

import java.util.List;

public record QuizPlayResponseDto (
        Long id,
        String title,
        List<QuestionPlayDto> questions,
        Integer duration
        ){
}
