package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.CreateCategoryDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.dto.category.UpdateCategoryDto;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import co.istad.y2.quizzy.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthService authService;
    public CategoryController(CategoryService categoryService,
                              AuthService authService){
        this.categoryService = categoryService;
        this.authService = authService;
    }
    @PostMapping
    public CategoryResponseDto createCategory(
            @Valid @RequestBody CreateCategoryDto createCategoryDto,
            @AuthenticationPrincipal User user) {
        return categoryService.createCategory(createCategoryDto, user);
    }
//
//    @PostMapping
//    public CategoryResponseDto createResponseDto(
//            @Valid @RequestBody CreateCategoryDto createCategoryDto){
//        User user = null;
//        return categoryService.createCategory(createCategoryDto,user);
//    }
    @GetMapping
    public Page<ListCategoryResponseDto> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        return categoryService.getAllCategoriesWithQuestionCount(page, size, search);
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
