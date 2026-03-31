package co.istad.y2.quizzy.service;


import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.CreateCategoryDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.dto.category.UpdateCategoryDto;
import co.istad.y2.quizzy.model.Category;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public CategoryResponseDto createCategory(CreateCategoryDto createCategoryDto, User user){
        Category category = new Category();
        category.setName(createCategoryDto.name());
        category.setCreatedBy(user);

        Category saved = categoryRepository.save(category);
        return new CategoryResponseDto(saved.getId(),saved.getName());
    }

    @Override
    public List<ListCategoryResponseDto> getAllCategoriesWithQuestionCount(){
        return categoryRepository.findAll().stream().
                map(c->new ListCategoryResponseDto(
                c.getId(),
                c.getName(),
                c.getQuestions().size()
        )).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryEntityById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category Not Found!"));
    }

    @Override
    public void deleteCategory(Long id){
        Category category = getCategoryEntityById(id);
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, UpdateCategoryDto updateCategoryDto){
        Category category = getCategoryEntityById(id);
        category.setName(updateCategoryDto.name());
        Category updated = categoryRepository.save(category);
        return new CategoryResponseDto(updated.getId(),updated.getName());
    }
}
