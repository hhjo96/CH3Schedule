package com.example.schedule.user.repository;

import com.example.schedule.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findByIdAndDeletedFalse(Long userId);

    List<User> findAllByDeletedFalse();

    boolean existsByIdAndDeletedFalse(Long Id);

    Optional<User> findByEmailAndDeletedFalse(String email);
}
