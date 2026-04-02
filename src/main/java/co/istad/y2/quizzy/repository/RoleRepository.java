package co.istad.y2.quizzy.repository;

import co.istad.y2.quizzy.model.Role;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role,Integer> {


    Optional<Role> findByName(String role);
}
