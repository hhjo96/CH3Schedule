package com.example.schedule.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScheduleUpdateRequest {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
