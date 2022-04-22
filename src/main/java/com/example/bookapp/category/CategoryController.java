package com.example.bookapp.category;

import com.example.bookapp.category.dto.CreateCategoryDto;
import com.example.bookapp.category.dto.exception.CategoryExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public @ResponseBody
    List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping("/add")
    public @ResponseBody
    Category createCategory(@Valid @RequestBody CreateCategoryDto categoryDto) {
        try {
            return categoryService.createCategory(categoryDto);
        } catch (CategoryExists ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with name " + categoryDto.name + " already exist", ex);
        }
    }
}
