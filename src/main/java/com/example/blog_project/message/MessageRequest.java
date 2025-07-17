package com.example.blog_project.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MessageRequest
{
    @NotBlank(message = "수신자는 필수입력 항목입니다.")
    private String receiver;

    @NotBlank(message = "제목은 필수입력 항목입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입력 항목입니다.")
    private String content;

    private Long parentId;
}
