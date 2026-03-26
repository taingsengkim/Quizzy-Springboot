package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer,Long> {
}
