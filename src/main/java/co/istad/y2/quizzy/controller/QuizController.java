package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
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
    public Quiz createQuiz(@RequestBody QuizCreateDto quizCreateDto) {
        Quiz createdQuiz = quizService.createQuiz(quizCreateDto);
        return createdQuiz;
    }





//    // Update existing quiz
//    @PutMapping("/{id}")
//    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id,
//                                           @RequestBody QuizCreateDto quizCreateDto) {
//        Quiz updatedQuiz = quizService.updateQuiz(id, quizCreateDto);
//        return ResponseEntity.ok(updatedQuiz);
//    }
//
//    // Delete quiz
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
//        quizService.deleteQuiz(id);
//        return ResponseEntity.noContent().build();
//    }
//
    // Get all quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.findAll();
        return ResponseEntity.ok(quizzes);
    }

//    // Get quiz by id
//    @GetMapping("/{id}")
//    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
//        return quizService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}