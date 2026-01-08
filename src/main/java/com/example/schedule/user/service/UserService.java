package com.example.schedule.user.service;

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
            User user = new User(request.getName(), request.getEmail(), request.getPassword());
            User savedUser = userRepository.save(user);

            return new UserSignUpResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
                    savedUser.getCreatedAt(), savedUser.getModifiedAt());
        } catch(DataIntegrityViolationException e){
            throw new IllegalStateException("Email already exists");
        }
    }

    //login
    @Transactional(readOnly = true)
    public SessionUser login(@Valid UserLoginRequest request) {
        User user = userRepository.findByEmailAndPasswordAndDeletedFalse(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new IllegalStateException("Invalid information"));

        return new SessionUser(user.getId(), user.getName(), user.getEmail());
    }

    //read user - all
    @Transactional(readOnly = true)
    public List<UserGetResponse> findAll(){
        List<User> users = userRepository.findAllByDeletedFalse();

        return users.stream().map(user -> new UserGetResponse(user.getId(), user.getName(), user.getEmail(),
                user.getCreatedAt(), user.getModifiedAt())).toList();
    }

    //read user - one
    @Transactional(readOnly = true)
    public UserGetResponse findOne(Long userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new IllegalStateException("User not found"));

        return new UserGetResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt());
    }

    //read user - admin, all
    @Transactional(readOnly = true)
    public List<UserGetResponse> findAdminAll(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserGetResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt())).toList();
    }

    //update user
    @Transactional
    public UserUpdateResponse update(Long userId, UserUpdateRequest request) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new IllegalStateException("User not found"));
        user.update(request.getName(), request.getEmail(), request.getPassword());

        return new UserUpdateResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getModifiedAt());

    }

    //delete user
    @Transactional
    public void delete(Long userId) {
        boolean existence = userRepository.existsByIdAndDeletedFalse(userId);
        if(!existence) throw new IllegalStateException("User not found");

        userRepository.deleteById(userId);
    }
}
