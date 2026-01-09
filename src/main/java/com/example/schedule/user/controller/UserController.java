package com.example.schedule.user.controller;

import com.example.schedule.user.dto.*;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signUp (@Valid @RequestBody UserSignUpRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest request, HttpSession session) {
        SessionUser sessionUser = userService.login(request);
        session.setAttribute("loginUser", sessionUser);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //read user - all
    @GetMapping("/users")
    public ResponseEntity<List<UserGetResponse>> getAll(){

        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    //read user - one
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserGetResponse> getOne(@PathVariable Long userId){

        return ResponseEntity.status(HttpStatus.OK).body(userService.findOne(userId));
    }

    //read user - admin, All
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserGetResponse>> getAdminAll(){

        return ResponseEntity.status(HttpStatus.OK).body(userService.findAdminAll());
    }

    //update user
    @PutMapping("/users")
    public ResponseEntity<UserUpdateResponse> updateUser(
            @Valid @SessionAttribute(name = "loginUser", required = false) SessionUser sessionUser,
            @RequestBody UserUpdateRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(userService.update(sessionUser.getId(), request));
    }

    //delete user - soft delete
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){

        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
