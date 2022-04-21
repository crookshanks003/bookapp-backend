package com.example.bookapp.user.response;

import com.example.bookapp.user.Role;
import com.example.bookapp.user.User;

public class UserResponse {
    private int id;

    private String name;

    private String email;

    private String rollNumber;

    private Role role;

    private int wallet;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.rollNumber = user.getRollNumber();
        this.role = user.getRole();
        this.wallet = user.getWallet();
    }
}
