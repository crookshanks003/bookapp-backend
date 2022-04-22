package com.example.bookapp.category.dto;

import javax.validation.constraints.NotBlank;

public class CreateCategoryDto {
    @NotBlank(message = "Name is required")
    public String name;
}
