package com.example.bookapp.user.response;

import com.example.bookapp.user.Role;
import com.example.bookapp.user.User;

public class UserResponse {
    public int id;

    public String name;

    public String email;

    public String rollNumber;

    public String role;

    public int wallet;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.rollNumber = user.getRollNumber();
        this.role = user.getRole().toString();
        this.wallet = user.getWallet();
    }
}
