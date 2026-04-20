package co.istad.y2.quizzy.repository;


import co.istad.y2.quizzy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("""
    SELECT c.id, c.name, COUNT(q), c.description, c.imageUrl
    FROM Category c
    LEFT JOIN c.quizzes q
    GROUP BY c.id, c.name, c.description, c.imageUrl
    """)
    Page<Object[]> findAllWithQuizCount(Pageable pageable);

    @Query("""
    SELECT c.id, c.name, COUNT(q), c.description, c.imageUrl
    FROM Category c
    LEFT JOIN c.quizzes q
    WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
    GROUP BY c.id, c.name, c.description, c.imageUrl
    """)
    Page<Object[]> findAllWithQuizCountAndSearch(String search, Pageable pageable);
}
