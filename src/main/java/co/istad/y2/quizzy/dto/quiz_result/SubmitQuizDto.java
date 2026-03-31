package co.istad.y2.quizzy.dto.quiz_result;


import java.util.List;

public record SubmitQuizDto (
    List<SubmitAnswerDto> answers
    ){
}
