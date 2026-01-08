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

    Optional<User> findByEmailAndPasswordAndDeletedFalse(@NotBlank @Email String email, @NotBlank(message = "비밀번호는 필수 입력 값입니다.") @Size(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.") @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "영문과 숫자를 모두 포함해야 합니다."
    ) String password);
}
