package com.example.bookapp.category.dto;

import com.example.bookapp.book.dto.BookPublishStatus;

import javax.validation.constraints.NotNull;

public class ChangeBookStatusDto {
    @NotNull(message = "Book id is required")
    public int bookId;

    @NotNull(message = "Book status is required")
    public BookPublishStatus status;
}
