package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.answer.AnswerResponseDto;
import co.istad.y2.quizzy.dto.question.CreateQuestionDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.dto.question.UpdateQuestionDto;
import co.istad.y2.quizzy.model.Answer;
import co.istad.y2.quizzy.model.Category;
import co.istad.y2.quizzy.model.Question;
import co.istad.y2.quizzy.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface QuestionService {

    QuestionResponseDto createQuestion(CreateQuestionDto createQuestionDto, User user);

    List<QuestionResponseDto> getAllQuestions();

//    List<QuestionResponseDto> getQuestionsByCategory(Long categoryId);

    QuestionResponseDto updateQuestion( UpdateQuestionDto dto);

    void deleteQuestion(Long id);

//    QuestionResponseDto getQuestionById(Long id);
}
