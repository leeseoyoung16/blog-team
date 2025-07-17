package com.example.blog_project.message;

import com.example.blog_project.CustomUserDetails;
import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController
{
    private final MessageService messageService;
    private final UserRepository userRepository;
    @PostMapping
    public ResponseEntity<Message> createMessage(@Valid @RequestBody MessageRequest messageRequest,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long senderId = userDetails.getUser().getId();
        User receiver = userRepository.findByUsername(messageRequest.getReceiver())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        messageService.send(messageRequest.getTitle(), messageRequest.getContent(),
                senderId, receiver.getId(), messageRequest.getParentId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<MessageResponse> getMessages(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        List<Message> messages = messageService.findAllMessagesByUser(userId);
        return messages.stream().map(MessageResponse::new).toList();
    }

    @GetMapping("/unread")
    public List<MessageResponse> getUnreadMessages(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long userId = userDetails.getUser().getId();
        List<Message> messages = messageService.findUnreadMessagesByUser(userId);
        return messages.stream().map(MessageResponse::new).toList();
    }

    @GetMapping("/sent")
    public List<MessageResponse> getSentMessages(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        List<Message> messages = messageService.findSentMessagesByUser(userId);
        return messages.stream().map(MessageResponse::new).toList();
    }

    @GetMapping("/received")
    public List<MessageResponse> getReceivedMessages(@AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long userId = userDetails.getUser().getId();
        List<Message> messages = messageService.findReceivedMessagesByUser(userId);
        return messages.stream().map(MessageResponse::new).toList();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable Long messageId, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long userId = userDetails.getUser().getId();
        Message message = messageService.findById(messageId, userId);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        messageService.delete(messageId, userId);
        return ResponseEntity.noContent().build();
    }
}
