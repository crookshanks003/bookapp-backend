package com.example.bookapp.book;

import com.example.bookapp.author.Author;
import com.example.bookapp.author.AuthorService;
import com.example.bookapp.book.dto.CreateBookDto;
import com.example.bookapp.book.exception.BookNotFound;
import com.example.bookapp.category.Category;
import com.example.bookapp.category.CategoryService;
import com.example.bookapp.user.User;
import com.example.bookapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public @ResponseBody
    Book getBookById(@PathVariable String id) {
        try {
            return bookService.getBookById(Integer.parseInt(id));
        } catch (BookNotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found", ex);
        }
    }

    @PostMapping("/add")
    public @ResponseBody
    Book addBook(@Valid @RequestBody CreateBookDto bookDto) {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        Category category = categoryService.getCategoryById(bookDto.categoryId);
        Author author = authorService.getAuthorById(bookDto.authorId);
        return bookService.createBook(bookDto, author, category, user);
    }
}
