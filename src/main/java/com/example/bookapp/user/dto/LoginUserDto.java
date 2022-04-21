package com.example.bookapp.user.dto;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;

public class LoginUserDto {
    @NotBlank(message = "Email is required")
    public String email;
    @NotBlank(message = "Password is required")
    public String password;
}
