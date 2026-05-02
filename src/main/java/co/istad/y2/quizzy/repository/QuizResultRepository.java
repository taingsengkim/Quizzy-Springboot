package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.QuizResult;
import co.istad.y2.quizzy.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult,Long> {
    List<QuizResult> findByUserId(Long id);
    Page<QuizResult> findByUser(User user, Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM quiz_result
        WHERE quiz_id IN (
            SELECT id FROM quiz
            WHERE category_id = :categoryId
        )
    """, nativeQuery = true)
    void deleteQuizResultsByCategoryId(@Param("categoryId") Long categoryId);

}



