package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.auth.LoginDto;
import co.istad.y2.quizzy.dto.auth.RegisterDto;
import co.istad.y2.quizzy.dto.auth.UserResponseDto;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAll(){
        return authService.findAll();
    }

    @GetMapping("/users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id){
        return authService.getUserDetail(id);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterDto dto){
        return authService.register(dto);
    }

    @PostMapping("/login")
    public Map<String,String> login(@Valid @RequestBody LoginDto dto){
        String token = authService.login(dto);
        return Map.of("token",token);
    }
}
