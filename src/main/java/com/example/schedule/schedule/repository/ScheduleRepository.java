package com.example.schedule.schedule.repository;

import com.example.schedule.schedule.entity.Schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    //삭제 안 된 스케줄 전체 조회
    List<Schedule> findAllByDeletedFalse();

    //스케줄 한개 조회
    Optional<Schedule> findByIdAndDeletedFalse(Long id);

    //스케줄의 존재 여부 조회
    boolean existsByIdAndDeletedFalse(Long id);

    //사용자의 스케줄 전체 조회
    List<Schedule> findAllByUserIdAndDeletedFalse(Long userId);

    //사용자의 스케줄 전체 조회 페이징
    Page<Schedule> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);

}
