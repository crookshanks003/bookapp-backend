package com.example.bookapp.book;

import com.example.bookapp.book.dto.BookPublishStatus;
import com.example.bookapp.book.dto.FeedBook;
import com.example.bookapp.category.Category;
import com.example.bookapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select new com.example.bookapp.book.dto.FeedBook(b) from Book b where b.id = ?1")
    Optional<FeedBook> findFeedBookById(int id);

    @Transactional
    @Modifying
    @Query("update Book b set b.publishStatus = ?1 where b.id=?2")
    void updateBookStatus(BookPublishStatus publishStatus, int id);

    @Query("select b.owner.id from Book b where id = ?1")
    int getOwnerId(int id);

    List<Book> findByNameContainingAndOwnerNot(String queryString, User owner);

    @Query("select b from Book b where b.name like %?1% and b.owner != ?2 and b.owner.active=1")
    List<Book> searchBook(String query, User user);

    @Query("select new com.example.bookapp.book.dto.FeedBook(b) from Book b where b.category in ?1 and b.owner.id != ?2 and b.checkedOut = false")
    List<FeedBook> findBookForFeed(List<Category> category, int userId);

    List<Book> findByOwner(User user);
}
