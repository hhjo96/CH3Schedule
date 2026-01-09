package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ServerException{

    public CommentNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
