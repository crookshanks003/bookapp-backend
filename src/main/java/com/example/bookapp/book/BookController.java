package com.example.bookapp.book;

import com.example.bookapp.author.Author;
import com.example.bookapp.author.AuthorService;
import com.example.bookapp.book.dto.CreateBookDto;
import com.example.bookapp.book.dto.FeedBook;
import com.example.bookapp.book.exception.BookCheckedOut;
import com.example.bookapp.book.exception.BookNotFound;
import com.example.bookapp.category.Category;
import com.example.bookapp.category.CategoryService;
import com.example.bookapp.category.dto.ChangeBookStatusDto;
import com.example.bookapp.transaction.exception.OwnerMismatchException;
import com.example.bookapp.user.User;
import com.example.bookapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

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
    FeedBook getBookById(@PathVariable String id) {
        try {
            return bookService.getFeedBookById(Integer.parseInt(id));
        } catch (BookNotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found", ex);
        }
    }

    @PostMapping("/add")
    public @ResponseBody
    Book addBook(@Valid @RequestBody CreateBookDto bookDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        Category category = categoryService.getCategoryById(bookDto.categoryId);
        Author author = authorService.getAuthorById(bookDto.authorId);
        return bookService.createBook(bookDto, author, category, user);
    }

    //DEPRECATED
    @PostMapping("/update/status")
    public @ResponseBody
    boolean changeBookStatus(@Valid @RequestBody ChangeBookStatusDto changeBookStatusDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            bookService.changePublishStatus(changeBookStatusDto);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to make this request");
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    boolean deleteBook(@PathVariable String id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        try {
            bookService.deleteBook(Integer.parseInt(id), user);
            return true;
        }catch (OwnerMismatchException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to make this request");
        }catch (BookNotFound ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with given id not found");
        } catch (BookCheckedOut ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to delete. Book already checked out");
        }
    }

    @GetMapping("/search")
    public @ResponseBody
    List<Book> searchBook(@RequestParam("query") String query){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        return bookService.searchBook(query ,user);
    }
}
