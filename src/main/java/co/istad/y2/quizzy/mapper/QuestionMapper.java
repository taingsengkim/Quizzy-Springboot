package co.istad.y2.quizzy.mapper;

import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.model.Question;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionResponseDto mapToResponse(Question question);

}
