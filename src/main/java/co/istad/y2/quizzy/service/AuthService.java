package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.auth.*;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.User;

import java.util.List;


public interface AuthService {
     User register(RegisterDto dto);
     LoginResponseDto login(LoginDto dto);
     List<AllUserResponseDto> findAll();
     List<UserResponseDto> findAllWithDetails();
     User getUserFromToken(String authHeader);
     UserResponseDto getUserProfile(String authHeader);
     User approveRole(Long id);
     List<Role> getAllRole();

     UserResponseDto getUserDetail(Long id);

     UserResponseDto updateProfile(String authHeader, UpdateProfileDto dto);
}
