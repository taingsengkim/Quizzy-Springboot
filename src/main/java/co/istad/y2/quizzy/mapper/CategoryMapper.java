package co.istad.y2.quizzy.mapper;


import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.model.Category;
import org.mapstruct.Mapper;

import java.lang.invoke.CallSite;
import java.util.List;

@Mapper (componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto mapToResponse(Category category);
    ListCategoryResponseDto mapToListResponse(Category category);
}
