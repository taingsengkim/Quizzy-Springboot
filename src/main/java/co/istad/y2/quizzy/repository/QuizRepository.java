package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
