package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ServerException{

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
