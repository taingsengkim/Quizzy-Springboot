package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.auth.LoginDto;
import co.istad.y2.quizzy.dto.auth.RegisterDto;
import co.istad.y2.quizzy.dto.auth.UserResponseDto;
import co.istad.y2.quizzy.exception.user.EmailAlreadyExist;
import co.istad.y2.quizzy.exception.user.InvalidCredentialsException;
import co.istad.y2.quizzy.exception.user.UserNotFound;
import co.istad.y2.quizzy.exception.user.UsernameAlreadyExist;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public AuthServiceImpl(UserRepository userRepository,
                       JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public User register(RegisterDto dto){
        if(userRepository.findByUsername(dto.username()).isPresent()){
            throw new UsernameAlreadyExist("Username '" + dto.username() + "' is already exist");
        }
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExist("Email '" + dto.email() + "' is already registered");
        }

        User user = new User();
        user.setRole(Role.ADMIN);
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setUsername(dto.username());
        user.setAvatar("https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png");
        return userRepository.save(user);
    }
    @Override
    public String login(LoginDto dto){
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(()->new InvalidCredentialsException("Invalid Username Or Password!"));

        if(!user.getPassword().equals(dto.password())){
            throw new InvalidCredentialsException("Invalid Username Or Password!");
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
                        u.getAvatar()
                )
        ).toList();
    }


    @Override
    public User getUserFromToken(String authHeader){
        String token = authHeader.replace("Bearer","").trim();
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Not Found!"));
    }

    @Override
    public UserResponseDto getUserDetail(Long id) {
        User user =userRepository.findById(id).orElseThrow(()->new UserNotFound("User with this id doesn't exist!"));

        return new UserResponseDto(user.getId(),user.getUsername(),user.getEmail(),user.getAvatar()) ;
    }

}
