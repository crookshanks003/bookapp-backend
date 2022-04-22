package com.example.bookapp.book;

import com.example.bookapp.book.dto.BookPublishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Transactional
    @Modifying
    @Query("update Book b set b.publishStatus = ?1 where b.id=?2")
    void updateBookStatus(BookPublishStatus publishStatus, int id);

    @Query("select b.owner.id from Book b where id = ?1")
    int getOwnerId(int id);
}
