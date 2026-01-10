package com.example.schedule.schedule.service;


import com.example.schedule.exception.ForbiddenException;
import com.example.schedule.exception.UserNotFoundException;
import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.schedule.repository.ScheduleRepository;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ScheduleCreateResponse save(Long userId, ScheduleCreateRequest request) {
        //세션으로 로그인 된 유저의 아이디이므로 현재는 유저 검증이 불필요하긴 하나 유저 엔티티가 필요하므로 그냥 두었음
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        Schedule schedule = ScheduleMapper.getScheduleInstance(user, request);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ScheduleMapper.getScheduleCreateResponseInstance(savedSchedule);
    }

    //read schedule - all, paging
    public Page<ScheduleGetResponse> getSchedulesWithPaging(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Schedule> pagedSchedule = scheduleRepository.findAllByUserIdAndDeletedFalse(userId, pageable);
        return pagedSchedule.map(ScheduleMapper::getScheduleGetResponseInstance);
    }


    //read schedule - all
    @Transactional(readOnly = true)
    public List<ScheduleGetResponse> findAllByUserId(Long userId) {
        //유저가 없다면 탈퇴한 사용자라고 뜬다
        //자기가 작성한 일정만 보임
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndDeletedFalse(userId);

        return schedules.stream()
                .map(ScheduleMapper::getScheduleGetResponseInstance).toList();
    }

    //read schedule - one
    @Transactional(readOnly = true)
    public ScheduleGetResponse findOne(Long userId, Long scheduleId) {
        //스케줄이 없는 경우 체크, 유저가 없는 경우 탈퇴한 사용자라고 뜸
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        //자기가 작성한 일정만 보임
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");
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
        //스케줄을 작성한 유저가 맞는지 검증
        //유저 자체의 검증은 세션에서 함
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        //자기가 작성한 일정만
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");

        schedule.update(request.getTitle(), request.getContent());

        return ScheduleMapper.getScheduleUpdateResponseInstance(schedule);
    }

    //delete schedule - soft delete, 로그인한 유저의 일정만 삭제 가능
    @Transactional
    public void delete(Long scheduleId, Long userId) {
        //스케줄을 작성한 유저가 맞는지 검증 후 삭제
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        //자기가 작성한 일정만
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");

        schedule.delete();
    }

    private Schedule findScheduleByIdAndDeletedFalseOrThrow(Long scheduleId){
        return scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new ScheduleNotFoundException("schedule not found"));
    }

}
