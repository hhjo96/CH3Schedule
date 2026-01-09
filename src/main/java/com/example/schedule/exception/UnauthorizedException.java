package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServerException{

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 틀립니다.");
    }
}
