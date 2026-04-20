package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizUpdateDto;
import co.istad.y2.quizzy.model.Quiz;
import co.istad.y2.quizzy.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public QuizResponseDto createQuiz( @Valid  @RequestBody QuizCreateDto quizCreateDto) {
        return quizService.createQuiz(quizCreateDto);
    }

    // Update existing quiz
    @PatchMapping("/{id}")
    public QuizResponseDto updateQuiz(@PathVariable Long id,
                                      @Valid @RequestBody QuizUpdateDto quizUpdateDto) {
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
    public ResponseEntity<Page<QuizResponseDto>> getAllQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(
                quizService.findAll(page, size, search, categoryId)
        );
    }

    // Get quiz by id
    @GetMapping("/{id}")
    public QuizResponseDto getQuizById(@PathVariable Long id) {
        return quizService.findById(id);
    }


    @GetMapping("/categories/{id}")
    public ResponseEntity<Page<QuizResponseDto>> getQuizByCategory(@PathVariable Long id,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(quizService.findByCategoryId(id, page, size));
    }


    //play quiz
    @GetMapping("/{id}/play")
    public QuizPlayResponseDto plyQuiz(@PathVariable Long id) {
        return quizService.getQuizForPlay(id);
    }

    @GetMapping("/{quizId}/questions/{questionId}/hint")
    public ResponseEntity<?> getHint(
            @PathVariable Long quizId,
            @PathVariable Long questionId,
            @RequestParam String attemptId
    ) {
        String hint = quizService.getHint(quizId, questionId, attemptId);
        return ResponseEntity.ok(hint);
    }
    @PostMapping("/{quizId}/reset-hints")
    public void resetHints(@RequestParam String attemptId) {
        quizService.resetHint(attemptId);
    }
    @PostMapping("/{quizId}/start-attempt")
    public ResponseEntity<Map<String, String>> startAttempt(@PathVariable Long quizId) {

        String attemptId = quizService.startAttempt(quizId);

        Map<String, String> response = new HashMap<>();
        response.put("attemptId", attemptId);

        return ResponseEntity.ok(response);
    }
}