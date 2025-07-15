package com.example.blog_project.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse
{
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int views;
    private int commentCount;

    public PostResponse (Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.views = post.getViews();
        this.commentCount = post.getComments().size();
    }
}
