package com.example.bookapp.transaction;

import com.example.bookapp.book.Book;
import com.example.bookapp.book.BookRepository;
import com.example.bookapp.transaction.dto.ChangeTransactionStatusDto;
import com.example.bookapp.transaction.dto.CreateTransactionDto;
import com.example.bookapp.transaction.dto.TransactionStatus;
import com.example.bookapp.transaction.exception.OwnerMismatchException;
import com.example.bookapp.transaction.exception.TransactionNotFound;
import com.example.bookapp.user.User;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction getTransactionById(int id) {
        return transactionRepository.findById(id).orElseThrow(TransactionNotFound::new);
    }

    public Transaction createTransaction(Book book, User user) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setBook(book);

        LocalDate currDate = LocalDate.now();

        transaction.setRequestedDate(currDate);
        transaction.setExpReturnDate(currDate.plusDays(14));

        transaction.setTransactionStatus(TransactionStatus.REQUESTED);

        return transactionRepository.save(transaction);
    }

    public void changeTransactionStatus(ChangeTransactionStatusDto transactionStatusDto, User user) {
        Transaction transaction = getTransactionById(transactionStatusDto.transactionId);
        if (transaction.getBook().getOwner().getId() == user.getId()) {
            transaction.setTransactionStatus(transactionStatusDto.transactionStatus);
            transactionRepository.save(transaction);
        } else {
            throw new OwnerMismatchException();
        }
    }

    public List<Transaction> getTransactionByUser(User user) {
        return transactionRepository.findByUser(user);
    }
}
