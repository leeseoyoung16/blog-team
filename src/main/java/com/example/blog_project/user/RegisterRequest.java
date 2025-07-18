package com.example.blog_project.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest
{
    @Size(min = 2, max = 15, message = "ID는 2자 이상 15자 이하여야 합니다.")
    @NotBlank(message = "ID는 필수입니다.")
    private String username;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Size(min = 5, max = 50)
    @Email(message = "올바른 이메일 형식이여야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
}
