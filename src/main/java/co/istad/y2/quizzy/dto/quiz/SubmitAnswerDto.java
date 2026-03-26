package co.istad.y2.quizzy.dto.quiz;

import java.util.List;

public record SubmitAnswerDto (
        Long questionId,
        List<Long> answerId
){
}
