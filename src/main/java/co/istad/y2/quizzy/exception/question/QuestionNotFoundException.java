package co.istad.y2.quizzy.exception.question;

import co.istad.y2.quizzy.exception.BusinessException;
import co.istad.y2.quizzy.service.QuestionService;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException  extends BusinessException {
    public QuestionNotFoundException(String msg){
        super(msg,HttpStatus.NOT_FOUND);
    }
}
