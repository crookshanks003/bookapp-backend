package com.example.bookapp.transaction;

import com.example.bookapp.book.Book;
import com.example.bookapp.transaction.dto.ChangeTransactionStatusDto;
import com.example.bookapp.transaction.dto.ExtensionStatus;
import com.example.bookapp.transaction.dto.TransactionStatus;
import com.example.bookapp.transaction.exception.OwnerMismatchException;
import com.example.bookapp.transaction.exception.ReturnDateAlreadyPassed;
import com.example.bookapp.transaction.exception.TransactionNotFound;
import com.example.bookapp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

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

        transaction.setExtensionStatus(ExtensionStatus.NONE);
        transaction.setTransactionStatus(TransactionStatus.REQUESTED);

        return transactionRepository.save(transaction);
    }

    public void changeTransactionStatus(ChangeTransactionStatusDto transactionStatusDto, User user, Transaction transaction) {
        if (Objects.equals(transaction.getBook().getOwner().getId(), user.getId()) || transactionStatusDto.transactionStatus == TransactionStatus.CANCELED) {
            transaction.setTransactionStatus(transactionStatusDto.transactionStatus);
            LocalDate currDate = LocalDate.now();
            if (transactionStatusDto.transactionStatus == TransactionStatus.BORROWED) {
                transaction.setExpReturnDate(currDate.plusDays(14));
                transaction.setLendDate(currDate);
            } else if (transactionStatusDto.transactionStatus == TransactionStatus.RETURNED) {
                transaction.setReturnDate(currDate);
                if (currDate.isAfter(transaction.getExpReturnDate())) {
                    int lateDay = Period.between(transaction.getExpReturnDate(), currDate).getDays();
                    transaction.setPenalty(lateDay);
                }
            }
            transactionRepository.save(transaction);
        } else {
            throw new OwnerMismatchException();
        }
    }

    public List<Transaction> getTransactionByUser(User user) {
        return transactionRepository.findByUserAndTransactionStatusNot(user, TransactionStatus.CANCELED);
    }

    public List<Transaction> getRequestsForUser(User user){
        return transactionRepository.findByOwner(user);
    }

    public void updateExtension(int transactionId) {
        if (LocalDate.now().isBefore(transactionRepository.findExpReturnDateById(transactionId))) {
            transactionRepository.updateExtensionStatus(ExtensionStatus.APPROVED, LocalDate.now().plusDays(7), transactionId);
        } else {
            throw new ReturnDateAlreadyPassed();
        }
    }

    public List<Transaction> getTransactionByBook(Book book) {
        return transactionRepository.findByBook(book);
    }

    public List<Transaction> getHistoryByUser(User user){
        return transactionRepository.findByUser(user);
    }

    public List<Transaction> getHistoryByOwner(User owner){
        return transactionRepository.findByBookOwner(owner);
    }
}
