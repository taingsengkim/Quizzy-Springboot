package co.istad.y2.quizzy.controller;


import co.istad.y2.quizzy.dto.quiz_result.*;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import co.istad.y2.quizzy.service.QuizServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizzes/result")
public class QuizResultController {
    private final QuizServiceResult quizServiceResult;
    private final AuthService authService;

    public QuizResultController(
            QuizServiceResult quizServiceResult,
            AuthService authService
    ){
        this.authService = authService;
        this.quizServiceResult = quizServiceResult;
    }

    @PostMapping("/submit")
    public QuizResultResponseDto submitQuizDto(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SubmitQuizDto submitQuizDto){

        User user = authService.getUserFromToken(authHeader);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        if (user.getRoles() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No roles");
        }        return quizServiceResult.submitQuiz(submitQuizDto,user);
    }

    @GetMapping("/history")
    public List<QuizResultHistoryDto> getHistory(
            @RequestHeader("Authorization") String authHeader) {
        User user = authService.getUserFromToken(authHeader);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        if (user.getRoles() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No roles");
        }
        return quizServiceResult.getUserHistory(user);
    }

    @GetMapping("/{id}")
    public QuizResultDetailDto getDetail(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        User user = authService.getUserFromToken(authHeader);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        if (user.getRoles() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No roles");
        }        return quizServiceResult.getResultDetail(id, user);
    }

//    @GetMapping
//    public List<QuizQuestionsDto> getQuiz(@RequestParam(name="category_id") Long categoryId){
//        return quizServiceResult.getQuizByCategory(categoryId);
//    }

}
