package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    Page<Question> findAll(Pageable pageable);
    List<Question> findByDifficulty(String difficulty);
}
