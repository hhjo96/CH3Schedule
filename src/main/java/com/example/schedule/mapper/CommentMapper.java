package com.example.schedule.mapper;

import com.example.schedule.comments.dto.CommentCreateRequest;
import com.example.schedule.comments.dto.CommentCreateResponse;
import com.example.schedule.comments.dto.CommentGetResponse;
import com.example.schedule.comments.dto.CommentUpdateResponse;
import com.example.schedule.comments.entity.Comment;
import com.example.schedule.schedule.entity.Schedule;
import com.example.schedule.user.entity.User;

//mapper 는 dto -> entity 변환, entity -> response dto 변환
public class CommentMapper {

    //생성자 방지
    private CommentMapper() {}

    public static Comment getCommentInstance(User user, Schedule schedule, CommentCreateRequest request) {

        return new Comment(user, schedule, request.getContent());
    }

    public static CommentCreateResponse getCommentCreateResponseInstance(Comment comment) {
        return new CommentCreateResponse(comment.getId(), comment.getSchedule().getId(), comment.getContent(),
                comment.getCreatedAt(), comment.getModifiedAt());
    }

    public static CommentGetResponse getCommentGetResponseInstance(Comment comment) {
        return new CommentGetResponse(comment.getId(), comment.getSchedule().getId(), comment.getSchedule().getTitle(),
                comment.getContent(), comment.getUser().displayDeletedUserName(), comment.getCreatedAt(), comment.getModifiedAt());
    }

    public static CommentUpdateResponse getCommentUpdateResponseInstance(Comment comment) {
        return new CommentUpdateResponse(comment.getId(), comment.getSchedule().getId(), comment.getSchedule().getTitle(), comment.getContent(),
                comment.getUser().displayDeletedUserName(), comment.getCreatedAt(), comment.getModifiedAt());
    }
}
