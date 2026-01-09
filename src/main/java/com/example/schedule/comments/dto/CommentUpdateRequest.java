package com.example.schedule.comments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotNull
    private String content;
}
