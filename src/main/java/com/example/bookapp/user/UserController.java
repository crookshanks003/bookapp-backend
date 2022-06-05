package com.example.bookapp.user;

import com.example.bookapp.book.Book;
import com.example.bookapp.book.BookService;
import com.example.bookapp.book.dto.FeedBook;
import com.example.bookapp.category.Category;
import com.example.bookapp.category.CategoryService;
import com.example.bookapp.user.auth.JwtUtils;
import com.example.bookapp.user.auth.UserDetailsServiceImpl;
import com.example.bookapp.user.dto.LoginUserDto;
import com.example.bookapp.user.dto.RegisterUserDto;
import com.example.bookapp.user.dto.updateCategoryDto;
import com.example.bookapp.user.exception.UserAlreadyExist;
import com.example.bookapp.user.exception.UserNotFound;
import com.example.bookapp.user.response.JwtResponse;
import com.example.bookapp.user.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @GetMapping("/test")
    public @ResponseBody
    String testRoute() {
        return "Hello World";
    }

    @GetMapping("/get")
    public @ResponseBody
    UserResponse getCurUser() {
        UserDetails authentication = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            User user = userService.getUserByEmail(authentication.getUsername());
            return new UserResponse(user);
        } catch (UserNotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", ex);
        }
    }

    @GetMapping("/book")
    public @ResponseBody
    List<Book> getUserBook(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        return bookService.getUserBook(user);
    }


    @PostMapping("/register")
    public @ResponseBody
    JwtResponse registerUser(@Valid @RequestBody RegisterUserDto userDto) {
        try {
            User user = userService.createUser(userDto, Role.USER);
            UserDetails userDetails = userService.mapUserToUserDetails(user);
            String jwt = jwtUtils.generateToken(userDetails);
            return new JwtResponse(jwt);
        } catch (UserAlreadyExist ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given email already exist");
        }
    }

    @PostMapping("/login")
    public @ResponseBody
    JwtResponse loginUser(@Valid @RequestBody LoginUserDto userDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.email, userDto.password));

        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password", ex);
        }

        User user = userService.getUserByEmail(userDto.email);
        if (user.isActive() == 0){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your account is suspended. Try contacting admin");
        }
        final UserDetails userDetails = userService.mapUserToUserDetails(user);
        final String jwt = jwtUtils.generateToken(userDetails);

        return new JwtResponse(jwt);
    }

    @PostMapping("/update/category")
    public @ResponseBody
    User updateCategories(@Valid @RequestBody updateCategoryDto categoryDto){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Category> allById = categoryService.getAllById(categoryDto.ids);
        return userService.changeCategories(allById, user);
    }

    @GetMapping("/is-admin")
    public @ResponseBody
    boolean isAdmin() {
        Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return roles.contains(new SimpleGrantedAuthority("ADMIN"));
    }

    @GetMapping("/feed")
    public @ResponseBody
    List<FeedBook> getFeed(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Category> categories = user.getCategories();
        return bookService.getFeed(categories, user.getId());
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    boolean deleteAdmin(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            int intId = Integer.parseInt(id);
            userService.deleteUser(intId);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to make this request");
        }
    }

    @GetMapping("/search")
    public @ResponseBody
    List<User> searchUser(@RequestParam("query") String query){
        return userService.searchUser(query);
    }
}
