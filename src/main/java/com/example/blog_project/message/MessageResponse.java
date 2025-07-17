package com.example.blog_project.message;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponse
{
    private Long id;
    private String title;
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime createdAt;
    private Boolean Isread;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.sender = message.getSender().getUsername();
        this.receiver = message.getReceiver().getUsername();
        this.createdAt = message.getCreatedAt();
        this.Isread = message.getIsRead();
    }
}
