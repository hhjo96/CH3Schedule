package com.example.schedule.exception;

import org.springframework.http.HttpStatus;

public class ScheduleNotFoundException extends ServerException{

    public ScheduleNotFoundException() {
        super(HttpStatus.NOT_FOUND, "일정이 없습니다.");
    }
}
