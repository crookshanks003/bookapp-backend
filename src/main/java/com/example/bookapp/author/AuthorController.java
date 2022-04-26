package com.example.bookapp.author;

import com.example.bookapp.author.dto.CreateAuthorDto;
import com.example.bookapp.author.exception.AuthorAlreadyExist;
import com.example.bookapp.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/author")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping("/{id}")
    public @ResponseBody
    Author getAuthor(@PathVariable String id){
        return authorService.getAuthorById(Integer.parseInt(id));
    }

    @GetMapping("/all")
    public @ResponseBody
    List<Author> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @PostMapping("/add")
    public @ResponseBody
    Author createAuthor(@Valid @RequestBody CreateAuthorDto authorDto) {
        try {
            return authorService.createAuthore(authorDto);
        } catch (AuthorAlreadyExist ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Author with name " + authorDto.name + " already exists");
        }
    }
}
