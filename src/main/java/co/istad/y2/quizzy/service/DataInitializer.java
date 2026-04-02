package co.istad.y2.quizzy.service;


import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init(){
        if(roleRepository.findByName("STUDENT").isEmpty()){
            roleRepository.save(new Role(1,"STUDENT"));
        }
        if(roleRepository.findByName("INSTRUCTOR").isEmpty()){
            roleRepository.save(new Role(2,"INSTRUCTOR"));
        }
        if(roleRepository.findByName("ADMIN").isEmpty()){
            roleRepository.save(new Role(3,"ADMIN"));
        }
    }
}
