package com.example.bookapp.user.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegisterUserDto {
    @NotBlank(message = "Name is required")
    public String name;

    @Email(message = "Invalid email")
    public String email;

    @Length(min = 6, message = "Password must be at least 6 characters long")
    public String password;

    @Length(min = 9, max = 9, message = "RollNumber must be of format f20yyxxxx")
    public String rollNumber;
}
