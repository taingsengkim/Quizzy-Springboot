package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizUpdateDto;
import co.istad.y2.quizzy.model.Quiz;
import co.istad.y2.quizzy.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public QuizResponseDto createQuiz(@RequestBody QuizCreateDto quizCreateDto) {
        return quizService.createQuiz(quizCreateDto);
    }





    // Update existing quiz
    @PatchMapping("/{id}")
    public QuizResponseDto updateQuiz(@PathVariable Long id,
                                           @RequestBody QuizUpdateDto quizUpdateDto) {
        return quizService.updateQuiz(id, quizUpdateDto);
    }

    // Delete quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // Get all quizzes
    @GetMapping
    public List<QuizResponseDto> getAllQuizzes() {
        return quizService.findAll();
    }

    // Get quiz by id
    @GetMapping("/{id}")
    public QuizResponseDto getQuizById(@PathVariable Long id) {
        return quizService.findById(id);
    }

    //play quiz
    @GetMapping("/{id}/play")
    public QuizPlayResponseDto playQuiz(@PathVariable Long id) {
        return quizService.getQuizForPlay(id);
    }
}