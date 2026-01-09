package com.example.schedule.user.repository;

import com.example.schedule.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    Optional<User> findByIdAndDeletedFalse(Long userId);

    List<User> findAllByDeletedFalse();

    boolean existsByIdAndDeletedFalse(Long Id);

    Optional<User> findByEmailAndPasswordAndDeletedFalse(String email, String password);
}
