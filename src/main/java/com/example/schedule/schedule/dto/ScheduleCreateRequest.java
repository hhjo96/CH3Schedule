package com.example.schedule.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScheduleCreateRequest {

    private String title;
    private String content;
    @NotNull
    @NotBlank
    private String userName;
}
