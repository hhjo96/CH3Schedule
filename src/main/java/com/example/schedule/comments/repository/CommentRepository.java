package com.example.schedule.comments.repository;

import com.example.schedule.comments.entity.Comment;
import com.example.schedule.schedule.dto.projection.CommentCountInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //삭제된 것 제외 댓글 id로 조회
    Optional<Comment> findByIdAndDeletedFalse(Long id);

    //삭제된 것 제외 로그인한 사용자의 스케줄의 댓글 all
    List<Comment> findAllByUserIdAndScheduleIdAndDeletedFalse(Long userId, Long scheduleId);

    //스케줄마다 댓글 개수 세기
    @Query("SELECT c.schedule.id AS scheduleId, count(c) AS commentCount " +
            "FROM Comment c " +
            "WHERE c.schedule.id IN :scheduleId AND c.deleted = false " +
            "GROUP BY c.schedule.id")
    List<CommentCountInterface> countByScheduleId(@Param("scheduleId") List<Long> scheduleId);
}
