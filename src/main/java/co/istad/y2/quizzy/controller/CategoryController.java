package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.category.CreateCategoryDto;
import co.istad.y2.quizzy.dto.category.ListCategoryResponseDto;
import co.istad.y2.quizzy.dto.category.UpdateCategoryDto;
import co.istad.y2.quizzy.exception.user.InvalidCredentialsException;
import co.istad.y2.quizzy.model.Role;
import co.istad.y2.quizzy.model.User;
import co.istad.y2.quizzy.service.AuthService;
import co.istad.y2.quizzy.service.CategoryService;
import co.istad.y2.quizzy.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
//    @PostMapping
//    public CategoryResponseDto createResponseDto(
//            @RequestHeader("Authorization") String authHeader,
//            @Valid @RequestBody CreateCategoryDto createCategoryDto){
//
//        User user = authService.getUserFromToken(authHeader);
//
//        if(user.getRole()!= Role.ADMIN){
//            throw new InvalidCredentialsException("Unauthorized");
//        }
//        System.out.println(user);
//        return categoryService.createCategory(createCategoryDto,user);
//    }

    @PostMapping
    public CategoryResponseDto createResponseDto(
            @Valid @RequestBody CreateCategoryDto createCategoryDto){

        User user = null;


        return categoryService.createCategory(createCategoryDto,user);
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
