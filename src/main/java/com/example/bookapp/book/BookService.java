package com.example.bookapp.book;

import com.example.bookapp.author.Author;
import com.example.bookapp.book.dto.BookPublishStatus;
import com.example.bookapp.book.dto.CreateBookDto;
import com.example.bookapp.book.dto.FeedBook;
import com.example.bookapp.book.exception.BookCheckedOut;
import com.example.bookapp.book.exception.BookNotFound;
import com.example.bookapp.category.Category;
import com.example.bookapp.category.dto.ChangeBookStatusDto;
import com.example.bookapp.transaction.exception.OwnerMismatchException;
import com.example.bookapp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public FeedBook getFeedBookById(int id){
        return bookRepository.findFeedBookById(id).orElseThrow(BookNotFound::new);
    }
    public Book getBookById(int id) {
        return bookRepository.findById(id).orElseThrow(BookNotFound::new);
    }

    public Book createBook(CreateBookDto bookDto, Author author, Category category, User owner) {
        Book book = new Book();
        book.setName(bookDto.name);
        book.setIsbn(bookDto.isbn);
        book.setOwner(owner);
        book.setAuthor(author);
        book.setYear(bookDto.year);
        book.setCategory(category);
        book.setPrice(15);
        book.setPublishStatus(BookPublishStatus.PUBLISHED);
        book.setCreatedDate(LocalDate.now());

        bookRepository.save(book);
        return book;
    }

    public void changePublishStatus(ChangeBookStatusDto bookStatusDto) {
        bookRepository.updateBookStatus(bookStatusDto.status, bookStatusDto.bookId);
    }

    public void deleteBook(int bookId, User user) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFound::new);
        if (book.isCheckedOut()){
            throw new BookCheckedOut();
        }
        if (book.getOwner() == user) {
            bookRepository.deleteById(bookId);
        } else {
            throw new OwnerMismatchException();
        }
    }

    public List<Book> searchBook(String query, User owner) {
        return bookRepository.findByNameContainingAndOwnerNot(query, owner);
    }

    public List<FeedBook> getFeed(List<Category> categories, int userId) {
        return bookRepository.findBookForFeed(categories, userId);
    }

    public void changeCheckedOut(Book book, boolean b) {
        book.setCheckedOut(b);
        bookRepository.save(book);
    }

    public List<Book> getUserBook(User user) {
        return bookRepository.findByOwner(user);
    }
}
