package com.example.bookapp.author;

import com.example.bookapp.book.Book;

import javax.persistence.*;
import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany
    private List<Book> books;
}
