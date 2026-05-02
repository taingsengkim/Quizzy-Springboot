package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.QuizResult;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.model.UserAnswer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer,Long> {
    List<UserAnswer> findByQuizResult(QuizResult quizResult);
    List<UserAnswer> findByQuizResult_Quiz_Category_Id(Long categoryId);
    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM user_answer_selected_answers
        WHERE selected_answers_id IN (
            SELECT a.id FROM answer a
            WHERE a.question_id IN (
                SELECT q.id FROM question q
                WHERE q.quiz_id IN (
                    SELECT qz.id FROM quiz qz
                    WHERE qz.category_id = :categoryId
                )
            )
        )
    """, nativeQuery = true)
    void deleteSelectedAnswersByCategoryId(@Param("categoryId") Long categoryId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM user_answer
        WHERE question_id IN (
            SELECT q.id FROM question q
            WHERE q.quiz_id IN (
                SELECT qz.id FROM quiz qz
                WHERE qz.category_id = :categoryId
            )
        )
    """, nativeQuery = true)
    void deleteUserAnswersByCategoryId(@Param("categoryId") Long categoryId);
}
