package co.istad.y2.quizzy.mapper;

import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")
public interface QuizMapper {
    @Mapping(target = "categoryId", source = "category.id")     // Maps Quiz.category.id to DTO categoryId
//    @Mapping(target = "createdByUsername", source = "createdBy.username")
    QuizResponseDto mapToResponse(Quiz quiz);
}
