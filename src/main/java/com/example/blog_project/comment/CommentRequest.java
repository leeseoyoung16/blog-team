package com.example.blog_project.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequest
{
    @NotBlank(message = "댓글 내용을 입력해 주세요")
    private String content;
}
