package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz_result.QuizQuestionsDto;
import co.istad.y2.quizzy.dto.quiz_result.QuizResultResponseDto;
import co.istad.y2.quizzy.dto.quiz_result.SubmitQuizDto;
import co.istad.y2.quizzy.model.User;

import java.util.List;

public interface QuizServiceResult {



    QuizResultResponseDto submitQuiz(SubmitQuizDto submitQuizDto, User user);

//    List<QuizQuestionsDto> getQuizByCategory(Long categoryId);
}
