package com.example.bookapp.category;

import com.example.bookapp.category.dto.CreateCategoryDto;
import com.example.bookapp.category.dto.exception.CategoryExists;
import com.example.bookapp.category.exception.CategoryNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryById(int id){
        return categoryRepository.findById(id).orElseThrow(CategoryNotFound::new);
    }

    public Category getCategoryByName(String name){
        return categoryRepository.findByName(name).orElseThrow(CategoryNotFound::new);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category createCategory(CreateCategoryDto categoryDto){
       try {
           Category category = getCategoryByName(categoryDto.name);
       } catch (CategoryNotFound ex){
           Category category = new Category();
           category.setName(categoryDto.name.toLowerCase());
           return categoryRepository.save(category);
       }
       throw new CategoryExists();
    }

    public List<Category> getAllById(List<Integer> ids){
        return categoryRepository.findAllById(ids);
    }
}
