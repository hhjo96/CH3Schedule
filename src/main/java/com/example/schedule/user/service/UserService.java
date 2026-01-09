package com.example.schedule.user.service;

import com.example.schedule.exception.DuplicatedEmailException;
import com.example.schedule.exception.UnauthorizedException;
import com.example.schedule.exception.UserNotFoundException;
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
            throw new DuplicatedEmailException("duplicated email");
        }
    }

    //login
    @Transactional(readOnly = true)
    public SessionUser login(@Valid UserLoginRequest request) {
        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("invalid email"));
        if(!user.getPassword().equals(request.getPassword())) throw new UnauthorizedException("invalid password");

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
        //세션으로 로그인 된 유저의 아이디이므로 현재는 유저 검증이 불필요하나
        //유저를 찾아야 하므로 우선 남겨둠
        User user = findUserByIdAndDeletedFalseOrThrow(userId);

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
        //세션으로 로그인 된 유저의 아이디이므로 현재는 유저 검증이 불필요
        User user = findUserByIdAndDeletedFalseOrThrow(userId);
        user.update(request.getName(), request.getEmail(), request.getPassword());

        return UserMapper.getUserUpdateResponseInstance(user);

    }

    //delete user - soft delete
    @Transactional
    public void delete(Long userId) {
        //삭제한 유저 이메일 재사용 가능하게 해야 함
        //유저 객체를 받아와서 deleted 만 처리하고, 이메일 처리까지 진행
        User user = findUserByIdAndDeletedFalseOrThrow(userId);

        user.delete();
    }

    private User findUserByIdAndDeletedFalseOrThrow(Long userId){
        return userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
    }
}
