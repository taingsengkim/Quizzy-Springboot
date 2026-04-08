package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.auth.*;
import co.istad.y2.quizzy.dto.quiz.UserQuizSummaryDto;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.model.UserStatus;
import co.istad.y2.quizzy.repository.RoleRepository;
import co.istad.y2.quizzy.repository.UserRepository;
import co.istad.y2.quizzy.repository.QuizResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final QuizResultRepository quizResultRepository; // For quiz stats

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           JwtUtil jwtUtil,
                           QuizResultRepository quizResultRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.quizResultRepository = quizResultRepository;
    }

    @Override
    public User register(RegisterDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username '" + dto.username() + "' already exists");
        }
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email '" + dto.email() + "' is already registered");
        }

        Role role = roleRepository.findByName(dto.role().toUpperCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setUsername(dto.username());
        user.setAvatar("https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_640.png");
        user.getRoles().add(role);

        user.setStatus(dto.role().equalsIgnoreCase("INSTRUCTOR") ? UserStatus.PENDING : UserStatus.APPROVED);

        return userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginDto dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password"));

        if (!user.getPassword().equals(dto.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }

        if (user.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("INSTRUCTOR")) &&
                user.getStatus() != UserStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your account is pending admin approval");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new LoginResponseDto(accessToken, refreshToken);
    }
    public List<AllUserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(u -> new AllUserResponseDto(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAvatar()
                ))
                .toList();
    }

    @Override
    public List<UserResponseDto> findAllWithDetails() {

        return userRepository.findAll().stream()
                .map(u -> new UserResponseDto(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAvatar(),
                        u.getRoles().stream().toList(),
                        u.getStatus(),
                        List.of() // Empty quiz history for this endpoint
                ))
                .toList();
    }

    @Override
    public User getUserFromToken(String authHeader) {
        String token = authHeader.replace("Bearer", "").trim();
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials"));
    }

    @Override
    public UserResponseDto getUserProfile(String authHeader) {
        User user = getUserFromToken(authHeader);

        // Fetch quiz history for this user
        List<UserQuizSummaryDto> quizHistory = quizResultRepository.findByUserId(user.getId())
                .stream()
                .map(result -> new UserQuizSummaryDto(
                        result.getQuiz().getId(),
                        result.getQuiz().getTitle(),
                        result.getQuiz().getQuestions().size(),
                        result.getScore()
                ))
                .toList();

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatar(),
                user.getRoles().stream().toList(),
                user.getStatus(),
                quizHistory
        );
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

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public UserResponseDto getUserDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this id doesn't exist!"));

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatar(),
                user.getRoles().stream().toList(),
                user.getStatus(),
                List.of()
        );
    }

}