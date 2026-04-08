package co.istad.y2.quizzy.dto.auth;


import co.istad.y2.quizzy.dto.quiz.UserQuizSummaryDto;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.UserStatus;

import java.util.List;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String avatar,
        List<Role> role,
        UserStatus status,
        List<UserQuizSummaryDto> quizHistory
) {
}
