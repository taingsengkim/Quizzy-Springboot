package co.istad.y2.quizzy.service;


import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.CreateCategoryDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.dto.category.UpdateCategoryDto;
import co.istad.y2.quizzy.mapper.CategoryMapper;
import co.istad.y2.quizzy.model.Category;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper){
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public CategoryResponseDto createCategory(CreateCategoryDto createCategoryDto, User user){
        Category category = new Category();
        category.setName(createCategoryDto.name());
        category.setCreatedBy(user);
        Category saved = categoryRepository.save(category);
        return categoryMapper.mapToResponse(saved);
    }

    @Override
    public List<ListCategoryResponseDto> getAllCategoriesWithQuestionCount(){
        return categoryRepository.findAll().stream()
                .map(categoryMapper::mapToListResponse).collect(Collectors.toList());
    }

//    @Override
//    public Category getCategoryEntityById(Long id){
//        return categoryRepository.findById(id)
//                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found!"));
//    }

    @Override
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found!"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, UpdateCategoryDto updateCategoryDto){
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found!"));
        category.setName(updateCategoryDto.name());
        Category updated = categoryRepository.save(category);
        return categoryMapper.mapToResponse(updated);
    }
}
