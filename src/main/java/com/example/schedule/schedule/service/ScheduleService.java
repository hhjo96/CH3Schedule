package com.example.schedule.schedule.service;


import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.schedule.repository.ScheduleRepository;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    //create schedule
    @Transactional
    public ScheduleCreateResponse save(ScheduleCreateRequest request) {

        User user = userRepository.findByIdAndDeletedFalse(request.getUserId()).orElseThrow(() -> new IllegalStateException(("User not found")));
        Schedule schedule = new Schedule(user, request.getTitle(), request.getContent());
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleCreateResponse(savedSchedule.getId(), savedSchedule.getTitle(), savedSchedule.getContent(), savedSchedule.getUser().getName(),
                savedSchedule.getCreatedAt(), savedSchedule.getModifiedAt());
    }

    //read schedule - all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAllByUserId(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndDeletedFalse(userId);

        return schedules.stream()
                .map(schedule -> new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(),
                        schedule.getUser().displayDeletedUserName(),
                schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    //read schedule - one
    @Transactional(readOnly = true)
    public ScheduleGetResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));

        return new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().displayDeletedUserName(),
                schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    //read schedule - admin, all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAdminAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(schedule -> new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().displayDeletedUserName(),
                        schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    //update schedule
    @Transactional
    public ScheduleUpdateResponse update(Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));
        schedule.update(request.getTitle(), request.getContent());

        return new ScheduleUpdateResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().displayDeletedUserName(),
                schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    //delete schedule - soft delete
    @Transactional
    public void delete(Long scheduleId) {
        boolean existence = scheduleRepository.existsByIdAndDeletedFalse(scheduleId);
        if(!existence) throw new IllegalStateException("Schedule not found");

        scheduleRepository.deleteById(scheduleId);
    }


}
