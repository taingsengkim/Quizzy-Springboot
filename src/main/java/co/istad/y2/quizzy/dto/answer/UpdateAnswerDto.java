package co.istad.y2.quizzy.dto.answer;

public record UpdateAnswerDto (
        Long id,
        String text,
        boolean correct
){
}
