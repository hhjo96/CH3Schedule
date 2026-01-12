package com.example.schedule.comments.controller;

import com.example.schedule.comments.dto.*;
import com.example.schedule.comments.service.CommentService;
import com.example.schedule.user.dto.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    //create comment
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CommentCreateResponse> createSchedule(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(sessionUser.getId(), scheduleId, request));
    }

    //read comment - all
    //로그인한 사람의 댓글 전체
    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<CommentGetResponse>> getAll(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId
            ){
        Long userId = sessionUser.getId();

        return ResponseEntity.status(HttpStatus.OK).body(commentService.findAllByUserId(userId, scheduleId));
    }

    //read comment - one
    @GetMapping("/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<CommentGetResponse> getOne(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long commentId){
        Long userId = sessionUser.getId();

        return ResponseEntity.status(HttpStatus.OK).body(commentService.findOne(userId, commentId));
    }

    //read comment - admin, All(soft delete 한 것 포함해서 가져오기)
    @GetMapping("/admin/comments")
    public ResponseEntity<List<CommentGetResponse>> getAdminAll(){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findAdminAll());
    }

    //update comment
    @PutMapping("/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(commentService.update(sessionUser.getId(), scheduleId, commentId, request));
    }

    //delete comment - soft delete, 로그인한 유저의 댓글만 삭제 가능
    @DeleteMapping("/schedules/{scheduleId}/comments/{commentId}")
    public ResponseEntity<Void> deletecomment(
            @Valid @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId) {
        Long userId = sessionUser.getId();

        commentService.delete(commentId, scheduleId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
