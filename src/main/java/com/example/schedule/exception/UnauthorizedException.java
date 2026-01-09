package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServerException{

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
