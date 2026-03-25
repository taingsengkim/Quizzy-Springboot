package co.istad.y2.quizzy.exception.user;

import co.istad.y2.quizzy.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFound extends BusinessException {
    public UserNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
