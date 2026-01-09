package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends ServerException{


    public DuplicatedEmailException() {
        super(HttpStatus.CONFLICT, "이메일이 중복됩니다");
    }
}
