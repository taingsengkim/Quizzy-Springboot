package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizUpdateDto;
import co.istad.y2.quizzy.model.Quiz;
import co.istad.y2.quizzy.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface QuizService {
    QuizResponseDto createQuiz(QuizCreateDto quizCreateDto);
    QuizResponseDto updateQuiz(Long id, QuizUpdateDto dto);
    void deleteQuiz(Long id);
    QuizResponseDto findById(Long id);
    List<QuizResponseDto> findAll();
    QuizPlayResponseDto getQuizForPlay(Long quizId);
    List<QuizPlayResponseDto> findByCategoryId(Long categoryId);
    boolean isCorrectAnswer(Long quizId, int questionIndex, String userAnswer);
    int totalQuestions(Long quizId);
    String getHint(Long quizId, Long questionId, String attempId);
    void resetHint(String attempId);
    String startAttempt(Long quizId);
}
