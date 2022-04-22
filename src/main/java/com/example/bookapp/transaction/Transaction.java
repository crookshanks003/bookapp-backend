package com.example.bookapp.transaction;

import com.example.bookapp.book.Book;
import com.example.bookapp.transaction.dto.ExtensionStatus;
import com.example.bookapp.transaction.dto.TransactionStatus;
import com.example.bookapp.user.User;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate requestedDate;

    @Column(nullable = true)
    private LocalDate lendDate;

    private LocalDate expReturnDate;

    @Column(nullable = true)
    private LocalDate returnDate;

    private int penalty;

    @OneToOne
    private User user;

    @OneToOne
    private Book book;

    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.ORDINAL)
    private ExtensionStatus extensionStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getLendDate() {
        return lendDate;
    }

    public void setLendDate(LocalDate lendDate) {
        this.lendDate = lendDate;
    }

    public LocalDate getExpReturnDate() {
        return expReturnDate;
    }

    public void setExpReturnDate(LocalDate expReturnDate) {
        this.expReturnDate = expReturnDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public ExtensionStatus getExtensionStatus() {
        return extensionStatus;
    }

    public void setExtensionStatus(ExtensionStatus extensionStatus) {
        this.extensionStatus = extensionStatus;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }
}
