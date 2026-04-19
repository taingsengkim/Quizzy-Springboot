package co.istad.y2.quizzy.controller;


import co.istad.y2.quizzy.dto.question.CreateQuestionDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.dto.question.UpdateQuestionDto;
import co.istad.y2.quizzy.jwt.JwtUtil;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import co.istad.y2.quizzy.service.QuestionServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

//    @PostMapping
//    public QuestionResponseDto createQuestion(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestBody CreateQuestionDto createQuestionDto ){
//
//        User user = authService.getUserFromToken(authHeader);
//        return questionService.createQuestion(createQuestionDto,user);
//    }
    @PostMapping
    public QuestionResponseDto createQuestion(
            @Valid @RequestBody CreateQuestionDto createQuestionDto ){
        User user = null;
        return questionService.createQuestion(createQuestionDto,user);
    }


    @GetMapping
    public ResponseEntity<Page<QuestionResponseDto>> getQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(questionService.getAllQuestions(page, size));
    }


    @PutMapping("/{id}")
    public QuestionResponseDto updateQuestion(
            @PathVariable  Long id,
            @Valid @RequestBody UpdateQuestionDto updateQuestionDto){

        log.info( " updateQuestionDto : " + updateQuestionDto);
        return questionService.updateQuestion(id,updateQuestionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

}
