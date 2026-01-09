package com.example.schedule.comments.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentCreateResponse {
    private final Long id;
    private final Long scheduleId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentCreateResponse(Long id, Long scheduleId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
