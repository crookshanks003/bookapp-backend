package com.example.bookapp.transaction;

import com.example.bookapp.book.Book;
import com.example.bookapp.transaction.dto.ExtensionStatus;
import com.example.bookapp.transaction.dto.TransactionStatus;
import com.example.bookapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
    @Transactional
    @Modifying
    @Query("update Transaction t set t.transactionStatus = ?1 where t.id=?2")
    void updateTransactionStatus(TransactionStatus transactionStatus, int id);

    @Transactional
    @Modifying
    @Query("update Transaction t set t.extensionStatus = ?1, t.expReturnDate=?2 where t.id=?3")
    void updateExtensionStatus(ExtensionStatus extensionStatus, LocalDate expReturnDate, int id);

    List<Transaction> findByUserAndTransactionStatusNot(User user, TransactionStatus status);

    @Query("select expReturnDate from Transaction where id=?1")
    LocalDate findExpReturnDateById(int id);

    @Query("select t from Transaction t where t.book.owner=?1 and t.transactionStatus != com.example.bookapp.transaction.dto.TransactionStatus.CANCELED")
    List<Transaction> findByOwner(User owner);

    List<Transaction> findByUser(User user);

    List<Transaction> findByBookOwner(User owner);

    List<Transaction> findByBook(Book book);
}
