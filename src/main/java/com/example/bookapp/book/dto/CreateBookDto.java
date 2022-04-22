package com.example.bookapp.book.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateBookDto {
    @NotBlank(message = "Name is required")
    public String name;

    @Length(min = 13, max = 13, message = "Invalid isbn")
    public String isbn;

    @Min(value = 1000, message = "Invalid year")
    @Max(value = 9999, message = "Invalid year")
    public int year;

    @NotNull(message = "Author is required")
    public int authorId;

    @NotNull(message = "Category is required")
    public int categoryId;
}
