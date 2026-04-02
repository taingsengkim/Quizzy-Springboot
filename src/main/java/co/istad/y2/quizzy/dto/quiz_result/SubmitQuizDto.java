package co.istad.y2.quizzy.dto.quiz_result;

import java.util.List;

public record SubmitQuizDto (
        Long quizId,               // the quiz being submitted
        Long duration,             // time in seconds or milliseconds
        List<SubmitAnswerDto> answers
) {}