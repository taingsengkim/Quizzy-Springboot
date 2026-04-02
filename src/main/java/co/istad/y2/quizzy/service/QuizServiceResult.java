package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz_result.*;
import co.istad.y2.quizzy.model.User;

import java.util.List;

public interface QuizServiceResult {



    QuizResultResponseDto submitQuiz(SubmitQuizDto submitQuizDto, User user);

    List<QuizResultHistoryDto> getUserHistory(User user);

    QuizResultDetailDto getResultDetail(Long resultId, User user);

//    List<QuizQuestionsDto> getQuizByCategory(Long categoryId);
}
