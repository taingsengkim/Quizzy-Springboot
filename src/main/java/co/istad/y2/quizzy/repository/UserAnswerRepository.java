package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.QuizResult;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer,Long> {
    List<UserAnswer> findByQuizResult(QuizResult quizResult);
}
