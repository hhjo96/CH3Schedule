package com.example.schedule.mapper;

import com.example.schedule.user.dto.SessionUser;
import com.example.schedule.user.dto.UserSignUpRequest;
import com.example.schedule.user.entity.User;

//mapper 는 dto -> entity 변환, entity -> response dto 변환
public class SessionUserMapper {

    private SessionUserMapper() {}

    public static SessionUser getSessionUserInstance(User user) {
        return new SessionUser(user.getId(), user.getName(), user.getEmail());
    }
}
