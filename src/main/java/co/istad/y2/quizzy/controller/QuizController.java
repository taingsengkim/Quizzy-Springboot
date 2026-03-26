package co.istad.y2.quizzy.controller;


import co.istad.y2.quizzy.dto.quiz.QuizQuestionsDto;
import co.istad.y2.quizzy.dto.quiz.QuizResultResponseDto;
import co.istad.y2.quizzy.dto.quiz.SubmitQuizDto;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import co.istad.y2.quizzy.service.QuizService;
import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {
    private final QuizService quizService;
    private final AuthService authService;

    public QuizController(
            QuizService quizService,
            AuthService authService
    ){
        this.authService = authService;
        this.quizService = quizService;
    }

    @PostMapping("/submit")
    public QuizResultResponseDto submitQuizDto(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SubmitQuizDto submitQuizDto){

        User user = authService.getUserFromToken(authHeader);
        return quizService.submitQuiz(submitQuizDto,user);
    }


    @GetMapping
    public List<QuizQuestionsDto> getQuiz(@RequestParam(name="category_id") Long categoryId){
        return quizService.getQuizByCategory(categoryId);
    }

}
