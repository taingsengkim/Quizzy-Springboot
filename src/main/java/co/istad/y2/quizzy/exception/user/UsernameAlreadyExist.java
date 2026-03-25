package co.istad.y2.quizzy.exception.user;

import co.istad.y2.quizzy.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExist extends BusinessException {
    public UsernameAlreadyExist(String msg){
        super(msg, HttpStatus.BAD_REQUEST);
    }
}
