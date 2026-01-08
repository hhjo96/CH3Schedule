package com.example.schedule.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    @NotNull
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "영문과 숫자를 모두 포함해야 합니다."
    )
    private String password;
}
