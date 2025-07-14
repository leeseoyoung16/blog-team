package com.example.blog_project.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequest
{
    @NotBlank(message = "제목은 필수입력 항목입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입력 항목입니다.")
    private String content;
}
