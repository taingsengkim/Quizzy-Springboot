package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.auth.LoginDto;
import co.istad.y2.quizzy.dto.auth.RegisterDto;
import co.istad.y2.quizzy.dto.auth.UserResponseDto;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.model.UserStatus;
import co.istad.y2.quizzy.repository.RoleRepository;
import co.istad.y2.quizzy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    public AuthServiceImpl(UserRepository userRepository,
                       JwtUtil jwtUtil, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
    }
    @Override
    public User register(RegisterDto dto){

        if(userRepository.findByUsername(dto.username()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username '" + dto.username() + "' is already exist");
        }
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email '" + dto.email() + "' is already registered");
        }
        // Assign Role
        Role role = roleRepository.findByName(dto.role().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));

        log.info("Role In AuthService : {}",role.getName());

        User user = new User();
//        user.setRole(Role.ADMIN);
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setUsername(dto.username());
        user.setAvatar("https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png");
        user.getRoles().add(role);
        // Only INSTRUCTOR is PENDING
        if(dto.role().equalsIgnoreCase("INSTRUCTOR")){
            user.setStatus(UserStatus.PENDING);
        } else {
            user.setStatus(UserStatus.APPROVED);
        }
        return userRepository.save(user);
    }
    @Override
    public String login(LoginDto dto){
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Username Or Password!"));

        if(!user.getPassword().equals(dto.password())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Username Or Password!");
        }
        if(user.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("INSTRUCTOR"))
                && user.getStatus() != UserStatus.APPROVED){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Your account is pending admin approval!");
        }
        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(
                u->new UserResponseDto(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAvatar(),
                        u.getRoles().stream().toList(),
                        u.getStatus()
                )
        ).toList();
    }


    @Override
    public User getUserFromToken(String authHeader){
        String token = authHeader.replace("Bearer","").trim();
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Credentials!"));
    }

    @Override
    public UserResponseDto getUserDetail(Long id) {
        User user =userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with this id doesn't exist!"));

        return new UserResponseDto(user.getId(),user.getUsername(),user.getEmail(),user.getAvatar(),user.getRoles().stream().toList(),user.getStatus()) ;
    }

    @Override
    public User approveRole(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        boolean isInstructor = user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase("INSTRUCTOR"));
        if (!isInstructor) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not an instructor");
        }
        user.setStatus(UserStatus.APPROVED);
        return userRepository.save(user);
    }

}
