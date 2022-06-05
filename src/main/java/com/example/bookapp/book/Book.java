package com.example.bookapp.book;


import com.example.bookapp.author.Author;
import com.example.bookapp.book.dto.BookPublishStatus;
import com.example.bookapp.category.Category;
import com.example.bookapp.user.Role;
import com.example.bookapp.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String isbn;

    private int year;

    private int price;

    @Enumerated(EnumType.ORDINAL)
    private BookPublishStatus publishStatus;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Author author;

    private boolean checkedOut = false;

    private LocalDate createdDate;

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }


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

    @ManyToOne
    private Category category;

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
