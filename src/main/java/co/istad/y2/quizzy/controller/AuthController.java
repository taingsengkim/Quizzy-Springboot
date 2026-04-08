package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.auth.*;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.model.UserStatus;
import co.istad.y2.quizzy.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil){
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // Get all users (no quiz history)
    @GetMapping("/users")
    public List<AllUserResponseDto> getAll(){
        return authService.findAll();
    }

    // Get specific user by ID (with quiz summary if you want)
    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id){
        return authService.getUserDetail(id); // Use getUserDetail for ID-based
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterDto dto){
        log.info("Register: {}", dto);
        return authService.register(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginDto dto){
        return authService.login(dto);
    }

    @GetMapping("/pending-instructors")
    public List<UserResponseDto> getPendingInstructors() {
        return authService.findAllWithDetails().stream()
                .filter(u -> u.role().stream().anyMatch(r -> r.getName().equalsIgnoreCase("INSTRUCTOR")))
                .filter(u -> u.status() == UserStatus.PENDING)
                .toList();
    }

    @PostMapping("/approve-instructor/{id}")
    public User approveInstructor(@PathVariable Long id){
        return authService.approveRole(id);
    }

    // Get profile of logged-in user (with quiz stats)
    @GetMapping("/profile")
    public UserResponseDto getProfile(@RequestHeader("Authorization") String authHeader) {
        return authService.getUserProfile(authHeader); // Call service to include quiz summary
    }
}
