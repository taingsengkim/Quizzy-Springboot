package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.model.Category;
import co.istad.y2.quizzy.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questions")
    List<Quiz> findAllWithQuestions();

    @Query("""
        SELECT q FROM Quiz q
        ORDER BY q.updatedAt DESC
        """)
    Page<Quiz> findAllSorted(Pageable pageable);

    @Query("""
        SELECT q FROM Quiz q
        WHERE (:search IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY q.updatedAt DESC
        """)
    Page<Quiz> searchByTitle(@Param("search") String search, Pageable pageable);

    @Query("""
        SELECT q FROM Quiz q
        WHERE q.category.id = :categoryId
        ORDER BY q.updatedAt DESC
        """)
    Page<Quiz> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("""
        SELECT q FROM Quiz q
        WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :search, '%'))
        AND q.category.id = :categoryId
        ORDER BY q.updatedAt DESC
        """)
    Page<Quiz> findByTitleAndCategory(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}

