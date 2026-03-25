package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.auth.LoginDto;
import co.istad.y2.quizzy.dto.auth.RegisterDto;
import co.istad.y2.quizzy.dto.auth.UserResponseDto;
import co.istad.y2.quizzy.exception.user.EmailAlreadyExist;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.lang.invoke.CallSite;
import java.util.List;


public interface AuthService {
     User register(RegisterDto dto);

     String login(LoginDto dto);

     List<UserResponseDto> findAll();

     User getUserFromToken(String authHeader);

     UserResponseDto getUserDetail(Long id);
}
