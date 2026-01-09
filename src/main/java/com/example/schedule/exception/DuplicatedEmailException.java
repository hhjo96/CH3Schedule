package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends ServerException{


    public DuplicatedEmailException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
