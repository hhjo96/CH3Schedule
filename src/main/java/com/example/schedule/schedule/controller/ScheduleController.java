package com.example.schedule.schedule.controller;

import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.service.ScheduleService;
import com.example.schedule.user.dto.*;
import com.example.schedule.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    //create schedule
    @PostMapping("/schedules")
    public ResponseEntity<ScheduleCreateResponse> createSchedule(@Valid @RequestBody ScheduleCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    //read schedule - all
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleGetResponse>> getAll(){

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll());
    }

    //read user - one
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleGetResponse> getOne(@PathVariable Long scheduleId){

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    //read user - admin, All
    @GetMapping("admin/schedules")
    public ResponseEntity<List<ScheduleGetResponse>> getAdminAll(){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAdminAll());
    }

    //update user
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleUpdateResponse> updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleUpdateRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, request));
    }

    //delete user
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId){

        scheduleService.delete(scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
