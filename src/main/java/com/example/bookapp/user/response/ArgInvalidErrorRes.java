package com.example.bookapp.user.response;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Map;

public class ArgInvalidErrorRes {
    public int status;
    public String message;
    public ArrayList<String> errors;
    public String path;

    public ArgInvalidErrorRes(int  status, String message, ArrayList<String> errors, String path) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.path = path;
    }
}
