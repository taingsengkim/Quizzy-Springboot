package co.istad.y2.quizzy.mapper;

import co.istad.y2.quizzy.dto.answer.AnswerPlayDto;
import co.istad.y2.quizzy.dto.question.QuestionPlayDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.model.Answer;
import co.istad.y2.quizzy.model.Question;
import co.istad.y2.quizzy.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")
public interface QuizMapper {
    @Mapping(target = "categoryId", source = "category.id")     // Maps Quiz.category.id to DTO categoryId
//    @Mapping(target = "createdByUsername", source = "createdBy.username")
    @Mapping(source = "maxHintsPerQuestion", target = "maxHintsPerQuestion")
    QuizResponseDto mapToResponse(Quiz quiz);



}
