package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ServerException{

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "다른 사용자의 일정입니다.");
    }
}
