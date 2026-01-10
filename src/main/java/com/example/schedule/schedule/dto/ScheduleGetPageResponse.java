package com.example.schedule.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleGetPageResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Long commentsCount;
    private final String userName;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public ScheduleGetPageResponse(Long id, String title, String content, Long commentsCount, String userName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.commentsCount = commentsCount;
        this.userName = userName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
