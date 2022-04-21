package com.example.bookapp.user.exception;

public class JwtMalformed extends RuntimeException {
    public JwtMalformed(){
        super("Invalid jwt");
    }
}
