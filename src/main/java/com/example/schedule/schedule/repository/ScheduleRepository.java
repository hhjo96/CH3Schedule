package com.example.schedule.schedule.repository;

import com.example.schedule.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByDeletedFalse();

    Optional<Schedule> findByIdAndDeletedFalse(Long id);

    boolean existsByIdAndDeletedFalse(Long id);

    List<Schedule> findAllByUserIdAndDeletedFalse(Long userId);
}
