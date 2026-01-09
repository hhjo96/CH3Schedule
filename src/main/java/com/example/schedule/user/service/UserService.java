package com.example.schedule.user.service;

import com.example.schedule.mapper.SessionUserMapper;
import com.example.schedule.mapper.UserMapper;
import com.example.schedule.user.dto.*;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //signUp
    @Transactional
    public UserSignUpResponse save(UserSignUpRequest request) {
        try {
            User user = UserMapper.getUserInstance(request);
            User savedUser = userRepository.save(user);

            return UserMapper.getUserSignUpResponseInstance(savedUser);
        } catch(DataIntegrityViolationException e){
            throw new IllegalStateException("Email already exists");
        }
    }

    //login
    @Transactional(readOnly = true)
    public SessionUser login(@Valid UserLoginRequest request) {
        User user = userRepository.findByEmailAndPasswordAndDeletedFalse(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new IllegalStateException("Invalid information"));

        return SessionUserMapper.getSessionUserInstance(user);
    }

//    //read user - all
//    @Transactional(readOnly = true)
//    public List<UserGetResponse> findAll(){
//        List<User> users = userRepository.findAllByDeletedFalse();
//
//        return users.stream().map(UserMapper::getUserGetResponseInstance).toList();
//    }

    //read user - one 로그인한 사람의 정보
    @Transactional(readOnly = true)
    public UserGetResponse findOne(Long userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new IllegalStateException("User not found"));

        return UserMapper.getUserGetResponseInstance(user);
    }

    //read user - admin, all
    @Transactional(readOnly = true)
    public List<UserGetResponse> findAdminAll(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::getUserGetResponseInstance).toList();
    }

    //update user
    @Transactional
    public UserUpdateResponse update(Long userId, UserUpdateRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new IllegalStateException("User not found"));
        user.update(request.getName(), request.getEmail(), request.getPassword());

        return UserMapper.getUserUpdateResponseInstance(user);

    }

    //delete user - soft delete
    @Transactional
    public void delete(Long userId) {
        boolean existence = userRepository.existsByIdAndDeletedFalse(userId);
        if(!existence) throw new IllegalStateException("User not found");

        userRepository.deleteById(userId);
    }
}
