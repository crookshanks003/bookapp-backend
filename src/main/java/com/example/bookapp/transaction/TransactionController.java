package com.example.bookapp.transaction;

import com.example.bookapp.book.Book;
import com.example.bookapp.book.BookService;
import com.example.bookapp.book.exception.BookNotFound;
import com.example.bookapp.transaction.dto.ChangeTransactionStatusDto;
import com.example.bookapp.transaction.dto.CreateTransactionDto;
import com.example.bookapp.transaction.exception.OwnerMismatchException;
import com.example.bookapp.user.User;
import com.example.bookapp.user.UserService;
import com.example.bookapp.user.auth.UserDetailsImpl;
import com.example.bookapp.user.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());

        return transactionService.getTransactionByUser(user);
    }

    @PostMapping("/add")
    public @ResponseBody
    Transaction createTransaction(@Valid @RequestBody CreateTransactionDto transactionDto){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            transactionService.changeTransactionStatus(transactionStatusDto, userDetails.getUserId());
            return true;
        } catch (OwnerMismatchException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not not authorized to make this request",ex);
        }
    }
}
