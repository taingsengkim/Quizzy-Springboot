package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz_result.*;
import co.istad.y2.quizzy.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuizServiceResult {



    QuizResultResponseDto submitQuiz(SubmitQuizDto submitQuizDto, User user);

    Page<QuizResultHistoryDto> getUserHistory(User user, int page, int size);

    QuizResultDetailDto getResultDetail(Long resultId, User user);

//    List<QuizQuestionsDto> getQuizByCategory(Long categoryId);
}
