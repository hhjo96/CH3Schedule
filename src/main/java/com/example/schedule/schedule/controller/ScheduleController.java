package com.example.schedule.schedule.controller;

import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.service.ScheduleService;
import com.example.schedule.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ScheduleCreateResponse> createSchedule(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody ScheduleCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(sessionUser.getId(), request));
    }

    //read schedule - all, paging
    //GET /schedules/mypage?page=0&size=10 하면 0번째페이지(첫번째페이지) 10개데이터
    @GetMapping("/schedules/mypage")
    public ResponseEntity<Page<ScheduleGetPageResponse>> getAllPaging(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.getSchedulesWithPaging(sessionUser.getId(), page, size));
    }


    //read schedule - all
    //로그인한 사람의 일정 전체
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleGetResponse>> getAll(@Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser){
        Long userId = sessionUser.getId();

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAllByUserId(userId));
    }

    //read user - one
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleGetResponse> getOne(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId){
        Long userId = sessionUser.getId();

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(userId, scheduleId));
    }

    //read user - admin, All(soft delete 한 것 포함해서 가져오기)
    @GetMapping("admin/schedules")
    public ResponseEntity<List<ScheduleGetResponse>> getAdminAll(){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAdminAll());
    }

    //update user
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleUpdateResponse> updateSchedule(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleUpdateRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(sessionUser.getId(), scheduleId, request));
    }

    //delete user - soft delete, 로그인한 유저의 일정만 삭제 가능
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId) {
        Long userId = sessionUser.getId();

        scheduleService.delete(scheduleId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
