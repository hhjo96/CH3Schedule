package com.example.schedule.comments.service;


import com.example.schedule.comments.dto.*;
import com.example.schedule.comments.entity.Comment;
import com.example.schedule.exception.CommentNotFoundException;
import com.example.schedule.mapper.CommentMapper;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.comments.repository.CommentRepository;
import com.example.schedule.exception.ForbiddenException;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.exception.UserNotFoundException;
import com.example.schedule.schedule.repository.ScheduleRepository;
import com.example.schedule.user.entity.User;
import com.example.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;


    //create comment
    @Transactional
    public CommentCreateResponse save(Long userId, Long scheduleId, CommentCreateRequest request) {
        //세션으로 로그인 된 유저의 아이디이므로 현재는 유저 검증이 불필요하긴 하나 엔티티가 필요하므로 그냥 두었음
        User user = findUserByIdAndDeletedFalseOrThrow(userId);
        //자기 스케줄만 보이므로 자기 스케줄만 댓글 가능
        Schedule schedule = findScheduleByIdAndDeletedFalseOrThrow(scheduleId);
        if(!schedule.getUser().getId().equals(userId)) throw new ForbiddenException("not your schedule");
        Comment comment = CommentMapper.getCommentInstance(user, schedule, request);
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.getCommentCreateResponseInstance(savedComment);
    }

    //read comment - 해당 일정의 댓글 all
    //
    @Transactional(readOnly = true)
    public List<CommentGetResponse> findAllByUserId(Long userId, Long scheduleId) {
        //세션으로 로그인 된 유저이므로 일정이 있는지, 댓글이 자기 것인지 검증
        boolean existSchedule = scheduleRepository.existsByIdAndDeletedFalse(scheduleId);
        if(!existSchedule) throw new ScheduleNotFoundException("schedule not found");

        //읽기는 전체 가능, 댓글이 유효한지는 deletedFalse 로 확인
        List<Comment> comments = commentRepository.findAllByUserIdAndScheduleIdAndDeletedFalse(userId, scheduleId);

        return comments.stream()
                .map(CommentMapper::getCommentGetResponseInstance).toList();
    }

    //read comment - 해당 일정의 댓글 one
    @Transactional(readOnly = true)
    public CommentGetResponse findOne(Long userId, Long commentId) {
        //세션으로 로그인 된 유저이므로 일정이 있는지만 검증
        boolean existSchedule = scheduleRepository.existsByIdAndDeletedFalse(commentId);
        if(!existSchedule) throw new ScheduleNotFoundException("schedule not found");

        //댓글이 존재하는지 검증
        Comment comment = findCommentByIdAndDeletedFalseOrThrow(commentId);

        //일정이 자기 것인지 검증
        if(!comment.getUser().getId().equals(userId)) throw new ForbiddenException("not your comment");

        return CommentMapper.getCommentGetResponseInstance(comment);
    }

    //read comment - admin, all
    @Transactional(readOnly = true)
    public List<CommentGetResponse> findAdminAll() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(CommentMapper::getCommentGetResponseInstance).toList();
    }

    //update comment
    @Transactional
    public CommentUpdateResponse update(Long userId, Long scheduleId, Long commentId, CommentUpdateRequest request) {
        //세션이므로 유저 검증 불필요
        //일정이 유효한지, 댓글이 유효한지, 댓글을 작성한 유저가 자신이 맞는지만 검증
        boolean existSchedule = scheduleRepository.existsByIdAndDeletedFalse(scheduleId);
        if(!existSchedule) throw new ScheduleNotFoundException("schedule not found");

        Comment comment = findCommentByIdAndDeletedFalseOrThrow(commentId);
        if(!comment.getUser().getId().equals(userId)) throw new ForbiddenException("not your comment");

        comment.update(request.getContent());

        return CommentMapper.getCommentUpdateResponseInstance(comment);
    }

    //delete comment - soft delete, 로그인한 유저의 댓글만 삭제 가능
    @Transactional
    public void delete(Long commentId, Long scheduleId, Long userId) {
        //일정이 존재하는지, 댓글이 존재하는지, 댓글을 작성한 유저가 맞는지만 검증
        boolean existSchedule = scheduleRepository.existsByIdAndDeletedFalse(scheduleId);
        if(!existSchedule) throw new ScheduleNotFoundException("schedule not found");

        Comment comment = findCommentByIdAndDeletedFalseOrThrow(commentId);
        if(!comment.getUser().getId().equals(userId)) throw new ForbiddenException("not your comment");

        comment.delete();
    }

    private Schedule findScheduleByIdAndDeletedFalseOrThrow(Long scheduleId){
        return scheduleRepository.findByIdAndDeletedFalse(scheduleId).orElseThrow(() -> new ScheduleNotFoundException("schedule not found"));
    }


    private User findUserByIdAndDeletedFalseOrThrow(Long userId){
        return userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
    }


    private Comment findCommentByIdAndDeletedFalseOrThrow(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(() -> new CommentNotFoundException("comment not found"));
    }
}
