package com.example.bookapp.transaction;

import com.example.bookapp.book.Book;
import com.example.bookapp.book.BookService;
import com.example.bookapp.book.dto.FeedBook;
import com.example.bookapp.book.exception.BookNotFound;
import com.example.bookapp.transaction.dto.ChangeTransactionStatusDto;
import com.example.bookapp.transaction.dto.CreateTransactionDto;
import com.example.bookapp.transaction.exception.OwnerMismatchException;
import com.example.bookapp.transaction.exception.ReturnDateAlreadyPassed;
import com.example.bookapp.user.User;
import com.example.bookapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public @ResponseBody
    List<Transaction> getTransactionByUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());

        return transactionService.getTransactionByUser(user);
    }

    @GetMapping("/by-book/{id}")
    public @ResponseBody
    List<Transaction> getTransactionByBook(@PathVariable String id){
        Book book = bookService.getBookById(Integer.parseInt(id));
        return transactionService.getTransactionByBook(book);
    }

    @PostMapping("/add")
    public @ResponseBody
    Transaction createTransaction(@Valid @RequestBody CreateTransactionDto transactionDto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            User user = userService.getUserByEmail(userDetails.getUsername());
            Book book = bookService.getBookById(transactionDto.bookId);
            return transactionService.createTransaction(book, user);
        } catch (BookNotFound ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @PostMapping("/update/status")
    public @ResponseBody
    boolean changeTransactionStatus(@Valid @RequestBody ChangeTransactionStatusDto transactionStatusDto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //TODO: Add userId to jwt claims to save this extra call to db
        User user = userService.getUserByEmail(userDetails.getUsername());
        try {
            transactionService.changeTransactionStatus(transactionStatusDto, user);
            return true;
        } catch (OwnerMismatchException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not not authorized to make this request",ex);
        }
    }

    @PostMapping("/update/extension")
    public @ResponseBody
    boolean changeExtensionStatus(@RequestBody() int transactionId){
        try{
            transactionService.updateExtension(transactionId);
            return true;
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist");
        } catch (ReturnDateAlreadyPassed ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot get extension as return date has already passed");
        }
    }
}
