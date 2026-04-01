package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.auth.LoginDto;
import co.istad.y2.quizzy.dto.auth.RegisterDto;
import co.istad.y2.quizzy.dto.auth.UserResponseDto;
import co.istad.y2.quizzy.model.User;

import java.util.List;


public interface AuthService {
     User register(RegisterDto dto);

     String login(LoginDto dto);

     List<UserResponseDto> findAll();

     User getUserFromToken(String authHeader);

     UserResponseDto getUserDetail(Long id);
}
