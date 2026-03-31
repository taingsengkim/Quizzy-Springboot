package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.model.Quiz;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface QuizService {
    Quiz createQuiz(QuizCreateDto quizCreateDto);
    Quiz updateQuiz(Long id, Quiz quiz);
    void deleteQuiz(Long id);
    Optional<Quiz> findById(Long id);
    List<Quiz> findAll();
    List<Quiz> findByCategory(Long categoryId);
}
