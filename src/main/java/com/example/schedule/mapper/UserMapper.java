package com.example.schedule.mapper;

import com.example.schedule.user.dto.UserGetResponse;
import com.example.schedule.user.dto.UserSignUpResponse;
import com.example.schedule.user.dto.UserUpdateResponse;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.dto.UserSignUpRequest;

//mapper 는 dto -> entity 변환, entity -> response dto 변환
public class UserMapper {

    private UserMapper() {}

    public static User getUserInstance(UserSignUpRequest request, String password) {
        return new User(request.getName(), request.getEmail(), request.getPassword());
    }

    public static UserSignUpResponse getUserSignUpResponseInstance(User user) {
        return new UserSignUpResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt());
    }

    public static UserGetResponse getUserGetResponseInstance(User user) {
        return new UserGetResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt());
    }

    public static UserUpdateResponse getUserUpdateResponseInstance(User user) {
        return new UserUpdateResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt());
    }
}
