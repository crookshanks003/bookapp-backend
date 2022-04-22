package com.example.bookapp.category.dto;

import com.example.bookapp.book.dto.BookPublishStatus;

import javax.validation.constraints.NotBlank;

public class ChangeBookStatusDto {
    @NotBlank(message = "Book id is required")
    public int bookId;

    @NotBlank(message = "Book status is required")
    public BookPublishStatus status;
}
