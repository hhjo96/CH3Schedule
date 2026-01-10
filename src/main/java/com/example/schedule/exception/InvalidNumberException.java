package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class InvalidNumberException extends ServerException{

    public InvalidNumberException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
