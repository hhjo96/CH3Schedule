package com.example.schedule.mapper;

import com.example.schedule.schedule.dto.*;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.user.entity.User;

//mapper 는 dto -> entity 변환, entity -> response dto 변환
public class ScheduleMapper {

    //생성자 방지
    private ScheduleMapper() {}

    public static Schedule getScheduleInstance(User user, ScheduleCreateRequest request) {

        return new Schedule(user, request.getTitle(), request.getContent());
    }

    public static ScheduleCreateResponse getScheduleCreateResponseInstance(Schedule schedule) {
        return new ScheduleCreateResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().getName(),
                schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    public static ScheduleGetResponse getScheduleGetResponseInstance(Schedule schedule) {
        return new ScheduleGetResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().displayDeletedUserName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    public static ScheduleGetPageResponse getScheduleGetPageResponseInstance(Schedule schedule, Long commentCount) {
        return new ScheduleGetPageResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(),commentCount, schedule.getUser().displayDeletedUserName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }

    public static ScheduleUpdateResponse getScheduleUpdateResponseInstance(Schedule schedule) {
        return new ScheduleUpdateResponse(schedule.getId(), schedule.getTitle(), schedule.getContent(), schedule.getUser().displayDeletedUserName(), schedule.getCreatedAt(), schedule.getModifiedAt());
    }
}
