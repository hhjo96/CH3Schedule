package com.example.schedule.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScheduleCreateRequest {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private Long userId;
}
