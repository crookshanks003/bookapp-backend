package com.example.bookapp.book;

import com.example.bookapp.author.Author;
import com.example.bookapp.book.dto.BookPublishStatus;
import com.example.bookapp.book.dto.CreateBookDto;
import com.example.bookapp.book.exception.BookNotFound;
import com.example.bookapp.category.Category;
import com.example.bookapp.category.dto.ChangeBookStatusDto;
import com.example.bookapp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookService {
    @Autowired
    private BookRepository bookRepository;

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
        book.setPublishStatus(BookPublishStatus.ADDED);

        bookRepository.save(book);
        return book;
    }

    public void changePublishStatus(ChangeBookStatusDto bookStatusDto){
        bookRepository.updateBookStatus(bookStatusDto.status, bookStatusDto.bookId);
    }
}
