package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ServerException{

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "사용자가 없습니다.");
    }
}
