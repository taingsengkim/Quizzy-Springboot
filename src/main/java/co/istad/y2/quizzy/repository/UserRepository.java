package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.dto.auth.LoginDto;
import co.istad.y2.quizzy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Long id(Long id);
}
