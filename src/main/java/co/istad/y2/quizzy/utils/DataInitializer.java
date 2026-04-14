//package co.istad.y2.quizzy.utils;
//
//
//import co.istad.y2.quizzy.model.*;
//import co.istad.y2.quizzy.repository.CategoryRepository;
//import co.istad.y2.quizzy.repository.QuizRepository;
//import co.istad.y2.quizzy.repository.RoleRepository;
//import co.istad.y2.quizzy.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//
//@Component
//public class DataInitializer {
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final CategoryRepository categoryRepository;
//    private final QuizRepository quizRepository;
//    public DataInitializer(RoleRepository roleRepository
//    ,UserRepository userRepository
//    ,CategoryRepository categoryRepository
//    ,QuizRepository quizRepository){
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.categoryRepository = categoryRepository;
//        this.quizRepository = quizRepository;
//    }
//
//    @PostConstruct
//    public void init(){
//        if(roleRepository.findByName("STUDENT").isEmpty()){
//            roleRepository.save(new Role(1,"STUDENT"));
//        }
//        if(roleRepository.findByName("INSTRUCTOR").isEmpty()){
//            roleRepository.save(new Role(2,"INSTRUCTOR"));
//        }
//        if(roleRepository.findByName("ADMIN").isEmpty()){
//            roleRepository.save(new Role(3,"ADMIN"));
//        }
//
//
//
//        User user = new User();
//
//        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
//            Role role = roleRepository.findByName("ADMIN")
//                    .orElseThrow(() -> new RuntimeException("Role not found"));            user.setUsername("admin");
//            user.setEmail("admin@gmail.com");
//            user.setPassword("123456");
//            user.setRoles(Set.of(role));
//            userRepository.save(user);
//        }
//        if (userRepository.findByEmail("kim040322@gmail.com").isEmpty()) {
//            Role role = roleRepository.findByName("ADMIN")
//                    .orElseThrow(() -> new RuntimeException("Role not found"));            user.setUsername("admin");
//            user.setEmail("kim040322@gmail.com");
//            user.setPassword("Kim123!@#");
//            user.setRoles(Set.of(role));
//            userRepository.save(user);
//        }
//
//        if (categoryRepository.findAll().isEmpty()) {
//            Category cat1 = new Category();
//            cat1.setName("Java Basics");
//            cat1.setDescription("Learn the fundamentals of Java programming.");
//            cat1.setImageUrl("https://siliconsoldier.gallerycdn.vsassets.io/extensions/siliconsoldier/java-with-bazel/0.3.2/1730876601943/Microsoft.VisualStudio.Services.Icons.Default");
//
//            Category cat2 = new Category();
//            cat2.setName("Web Development");
//            cat2.setDescription("Learn HTML, CSS, JavaScript, and frameworks.");
//            cat2.setImageUrl("https://creativetechpark.com/wp-content/uploads/Web-Development-Company-in-Bangladesh-4-400x400.png");
//
//            Category cat3 = new Category();
//            cat3.setName("Data Structures");
//            cat3.setDescription("Learn how to organize and manage data efficiently.");
//            cat3.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRm4Fy1kCgzWwcyCUesAaOuxKNuWRCx-TfYFA&s");
//
//            categoryRepository.saveAll(List.of(cat1, cat2, cat3));
//        }
//    }
//}
