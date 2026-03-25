package co.istad.y2.quizzy.controller;


import co.istad.y2.quizzy.dto.question.CreateQuestionDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.dto.question.UpdateQuestionDto;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import co.istad.y2.quizzy.service.QuestionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionServiceImpl questionService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public QuestionController(QuestionServiceImpl questionService, JwtUtil jwtUtil, AuthService authService){
        this.questionService = questionService;
        this.jwtUtil=jwtUtil;
        this.authService = authService;
    }

    @PostMapping
    public QuestionResponseDto createQuestion(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateQuestionDto createQuestionDto ){

        User user = authService.getUserFromToken(authHeader);
        return questionService.createQuestion(createQuestionDto,user);
    }

    @GetMapping()
    public List<QuestionResponseDto> getQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public QuestionResponseDto getQuestionById(@PathVariable Long id){
        return questionService.getQuestionById(id);
    }


    @GetMapping("/category/{categoryId}")
    public List<QuestionResponseDto> getQuestionsByCategory(@PathVariable Long categoryId){
        return questionService.getQuestionsByCategory(categoryId);
    }

    @PutMapping("/{id}")
    public QuestionResponseDto updateQuestion(@PathVariable Long id,
                                              @RequestBody UpdateQuestionDto updateQuestionDto,@RequestParam Long userId){

        log.info("Id : " + id + " | updateQuestionDto : " + updateQuestionDto);
        return questionService.updateQuestion(id,updateQuestionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id){
        questionService.deleteQuestion(id);
    }
}
