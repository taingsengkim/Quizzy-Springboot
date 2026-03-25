package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.CreateCategoryDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.dto.category.UpdateCategoryDto;
import co.istad.y2.quizzy.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryDto createCategoryDto);
    List<ListCategoryResponseDto> getAllCategoriesWithQuestionCount();
    Category getCategoryEntityById(Long id);
    void deleteCategory(Long id);
    CategoryResponseDto updateCategory(Long id, UpdateCategoryDto updateCategoryDto);

}
