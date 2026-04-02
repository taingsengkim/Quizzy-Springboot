package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.QuizResult;
import co.istad.y2.quizzy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult,Long> {
    List<QuizResult> findByUserId(Long id);
    List<QuizResult> findByUser(User user);
}
