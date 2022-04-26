package com.example.bookapp.book.dto;

import com.example.bookapp.author.Author;
import com.example.bookapp.book.Book;
import com.example.bookapp.category.Category;
import com.example.bookapp.user.User;
import com.example.bookapp.user.response.UserResponse;

import javax.persistence.*;
import java.time.LocalDate;

public class FeedBook {
    private Integer id;

    private String name;

    private String isbn;

    private int year;

    private int price;

    private BookPublishStatus publishStatus;

    private UserResponse owner;

    private Author author;

    private Category category;

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    private LocalDate createdDate;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public BookPublishStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(BookPublishStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public UserResponse getOwner() {
        return owner;
    }

    public void setOwner(UserResponse owner) {
        this.owner = owner;
    }

    public FeedBook(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.isbn = book.getIsbn();
        this.year = book.getYear();
        this.price = book.getPrice();
        this.publishStatus = book.getPublishStatus();
        this.owner = new UserResponse(book.getOwner());
        this.author = book.getAuthor();
        this.category = book.getCategory();
        this.createdDate = book.getCreatedDate();
    }
}
