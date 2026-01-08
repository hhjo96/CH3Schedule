package com.example.schedule.user.controller;

import com.example.schedule.user.dto.*;
import com.example.schedule.user.service.UserService;
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

    //create user
    @PostMapping("/users")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
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
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserUpdateResponse> updateUser(@Valid @PathVariable Long userId, @RequestBody UserUpdateRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, request));
    }

    //delete user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){

        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
