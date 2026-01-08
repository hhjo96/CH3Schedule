package com.example.schedule.schedule.service;


import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleCreateResponse save(ScheduleCreateRequest request) {
        Schedule schedule = new Schedule(request.getUserName(), request.getTitle(), request.getContent());
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleCreateResponse(savedSchedule.getId(), savedSchedule.getTitle(), savedSchedule.getContent(), savedSchedule.getUserName(),
                savedSchedule.getCreatedAt(), savedSchedule.getModifiedAt());
    }

    public List<ScheduleGetResponse> findAll() {
        List<Schedule> schedules = scheduleRepository.findAllByDeletedFalse();

        return schedules.stream()
                .map(schedule -> new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUserName(),
                schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    public ScheduleGetResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));

        return new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUserName(),
                schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    public List<ScheduleGetResponse> findAdminAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(schedule -> new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUserName(),
                        schedule.getCreatedAt(), schedule.getModifiedAt())).toList();
    }

    public ScheduleUpdateResponse update(Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));
        schedule.update(request.getTitle(), request.getContent());

        return new ScheduleUpdateResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUserName(),
                schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    public void delete(Long scheduleId) {
        boolean existence = scheduleRepository.existsByIdAndDeletedFalse(scheduleId);
        if(!existence) throw new IllegalStateException("Schedule not found");

        scheduleRepository.deleteById(scheduleId);
    }


}
