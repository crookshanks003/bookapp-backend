package com.example.bookapp.author.dto;

import javax.validation.constraints.NotBlank;

public class CreateAuthorDto {
    @NotBlank(message = "Author name is required")
    public String name;
}
