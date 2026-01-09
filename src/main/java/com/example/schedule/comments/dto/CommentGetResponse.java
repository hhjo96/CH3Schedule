package com.example.schedule.comments.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentGetResponse {
    private final Long id;
    private final Long scheduleId;
    private final String scheduleTitle;
    private final String content;
    private final String userName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentGetResponse(Long id, Long scheduleId, String scheduleTitle, String content, String userName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.scheduleTitle = scheduleTitle;
        this.content = content;
        this.userName = userName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
