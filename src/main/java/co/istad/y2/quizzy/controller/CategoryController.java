package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.CreateCategoryDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.dto.category.UpdateCategoryDto;
import co.istad.y2.quizzy.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryServiceImpl categoryService;
    public CategoryController(CategoryServiceImpl categoryService ){
        this.categoryService = categoryService;
    }
    @PostMapping
    public CategoryResponseDto createResponseDto(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateCategoryDto createCategoryDto){
        return categoryService.createCategory(createCategoryDto);
    }

    @GetMapping
    public List<ListCategoryResponseDto> getAllCategories(){
        return categoryService.getAllCategoriesWithQuestionCount();
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }
    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                                             @Valid @RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(id,updateCategoryDto);
    }
}
