package com.example.schedule.comments.repository;

import com.example.schedule.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByDeletedFalse();

    Optional<Comment> findByIdAndDeletedFalse(Long id);

    boolean existsByIdAndDeletedFalse(Long id);

    List<Comment> findAllByUserIdAndDeletedFalse(Long userId);

    List<Comment> findAllByUserIdAndScheduleIdAndDeletedFalse(Long userId, Long scheduleId);
}
