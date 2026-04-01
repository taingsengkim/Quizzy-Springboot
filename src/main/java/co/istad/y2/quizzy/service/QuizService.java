package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.model.Quiz;
import co.istad.y2.quizzy.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface QuizService {
    QuizResponseDto createQuiz(QuizCreateDto quizCreateDto);
    Quiz updateQuiz(Long id, Quiz quiz);
    void deleteQuiz(Long id);
    QuizResponseDto findById(Long id);
    List<QuizResponseDto> findAll();
    List<Quiz> findByCategory(Long categoryId);
}
