package com.example.bookapp.author;

import com.example.bookapp.author.dto.CreateAuthorDto;
import com.example.bookapp.author.exception.AuthorAlreadyExist;
import com.example.bookapp.author.exception.AuthorNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorById(int id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFound::new);
    }

    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name).orElseThrow(AuthorNotFound::new);
    }

    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    public Author createAuthore(CreateAuthorDto authorDto) {
        try {
            Author author = getAuthorByName(authorDto.name.toLowerCase());
        } catch (AuthorNotFound ex) {
            Author author = new Author();

            author.setName(authorDto.name.toLowerCase());

            return authorRepository.save(author);

        }
        throw new AuthorAlreadyExist();
    }
}
