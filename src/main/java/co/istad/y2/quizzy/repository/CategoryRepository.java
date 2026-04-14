package co.istad.y2.quizzy.repository;


import co.istad.y2.quizzy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("""
        SELECT c.id, c.name, COUNT(q.id), c.description, c.imageUrl
        FROM Category c
        LEFT JOIN c.quizzes q
        GROUP BY c.id, c.name, c.description, c.imageUrl
        """)
    List<Object[]> findAllWithQuizCount();
}
