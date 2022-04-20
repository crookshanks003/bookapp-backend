package com.example.bookapp.user;

import com.example.bookapp.user.auth.JwtUtils;
import com.example.bookapp.user.auth.UserDetailsServiceImpl;
import com.example.bookapp.user.dto.LoginUserDto;
import com.example.bookapp.user.dto.RegisterUserDto;
import com.example.bookapp.user.exception.UserAlreadyExist;
import com.example.bookapp.user.exception.UserNotFound;
import com.example.bookapp.user.response.JwtResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/test")
    public @ResponseBody
    String testRoute() {
        return "Hello World";
    }

    @GetMapping("/get/{id}")
    public @ResponseBody
    User getUser(@PathVariable int id) {
        try {
            return userService.getUserById(id);
        } catch (UserNotFound ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found", ex);
        }
    }

    @PostMapping("/register")
    public @ResponseBody
    JwtResponse registerUser(@RequestBody RegisterUserDto userDto) {
        try {
            UserDetails user = userService.createUser(userDto);
            String jwt = jwtUtils.generateToken(user);
            return new JwtResponse(jwt);
        } catch (UserAlreadyExist ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given email already exist");
        }
    }

    @PostMapping("/login")
    public @ResponseBody
    JwtResponse loginUser(@RequestBody LoginUserDto userDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.email, userDto.password));

        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password", ex);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.email);
        final String jwt = jwtUtils.generateToken(userDetails);

        return new JwtResponse(jwt);
    }
}
