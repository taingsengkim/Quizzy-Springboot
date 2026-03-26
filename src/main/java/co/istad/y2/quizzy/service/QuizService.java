package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz.QuizQuestionsDto;
import co.istad.y2.quizzy.dto.quiz.QuizResultResponseDto;
import co.istad.y2.quizzy.dto.quiz.SubmitQuizDto;
import co.istad.y2.quizzy.model.Question;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.repository.QuizResultRepository;

import java.util.List;

public interface QuizService {



    QuizResultResponseDto submitQuiz(SubmitQuizDto submitQuizDto, User user);

    List<QuizQuestionsDto> getQuizByCategory(Long categoryId);
}
