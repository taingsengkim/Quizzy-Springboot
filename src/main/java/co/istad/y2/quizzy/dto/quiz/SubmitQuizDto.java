package co.istad.y2.quizzy.dto.quiz;


import java.util.List;

public record SubmitQuizDto (
    List<SubmitAnswerDto> answers
    ){
}
