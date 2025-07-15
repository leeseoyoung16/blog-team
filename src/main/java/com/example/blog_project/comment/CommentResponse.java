package com.example.blog_project.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse
{
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long postid;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.postid = comment.getPost().getId();
    }
}
