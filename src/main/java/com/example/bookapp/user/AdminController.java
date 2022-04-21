package com.example.bookapp.user;

import com.example.bookapp.user.dto.RegisterUserDto;
import com.example.bookapp.user.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponse registerAdmin(@Valid RegisterUserDto userDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            User user =  userService.createUser(userDto, Role.ADMIN);
            return new UserResponse(user);
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to make this request");
        }
    }
}
