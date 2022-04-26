package com.example.bookapp.user.dto;

import com.example.bookapp.category.Category;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

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
