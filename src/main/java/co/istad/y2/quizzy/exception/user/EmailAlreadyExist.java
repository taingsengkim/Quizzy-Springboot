package co.istad.y2.quizzy.exception.user;

import co.istad.y2.quizzy.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExist extends BusinessException {
    public EmailAlreadyExist(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
