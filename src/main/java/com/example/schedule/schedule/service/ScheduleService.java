package com.example.schedule.schedule.service;


import com.example.schedule.mapper.ScheduleMapper;
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
        Schedule schedule = ScheduleMapper.getScheduleInstance(user, request);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleMapper.getScheduleCreateResponseInstance(savedSchedule);
    }

    //read schedule - all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAllByUserId(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndDeletedFalse(userId);

        return schedules.stream()
                .map(ScheduleMapper::getScheduleGetResponseInstance).toList();
    }

    //read schedule - one
    @Transactional(readOnly = true)
    public ScheduleGetResponse findOne(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));
        if(!userRepository.existsByIdAndDeletedFalse(userId)) throw new IllegalStateException("User not found");
        if(!schedule.getUser().getId().equals(userId)) throw new IllegalStateException("You can't see other user's schedule");
        return ScheduleMapper.getScheduleGetResponseInstance(schedule);
    }

    //read schedule - admin, all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAdminAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleMapper::getScheduleGetResponseInstance).toList();
    }

    //update schedule
    @Transactional
    public ScheduleUpdateResponse update(Long userId, Long scheduleId, ScheduleUpdateRequest request) {
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));
        if(!schedule.getUser().getId().equals(userId)) throw new IllegalStateException("You can't update other user's schedule");
        schedule.update(request.getTitle(), request.getContent());

        return ScheduleMapper.getScheduleUpdateResponseInstance(schedule);
    }

    //delete schedule - soft delete, 로그인한 유저의 일정만 삭제 가능
    @Transactional
    public void delete(Long scheduleId, Long userId) {
        //현재 유저가 삭제하려는 일정의 작성자인지 확인 후 삭제
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new IllegalStateException("Schedule not found"));
        if(!schedule.getUser().getId().equals(userId)) throw new IllegalStateException("You can't delete other user's schedule");

        scheduleRepository.deleteById(scheduleId);
    }


}
