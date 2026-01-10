package com.example.schedule.schedule.dto.projection;

import lombok.Getter;

@Getter
public class CommentCountDto implements CommentCountInterface {
    private final Long scheduleId;
    private final Long commentCount;

    public CommentCountDto(Long scheduleId, Long commentCount) {
        this.scheduleId = scheduleId;
        this.commentCount = commentCount;
    }
}
