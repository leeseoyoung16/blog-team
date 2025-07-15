package com.example.blog_project.comment;

import com.example.blog_project.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController
{
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        commentService.create(postId, userId, commentRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId)
    {
        List<Comment> comment = commentService.findByPostId(postId);
        List<CommentResponse> commentResponse = comment.stream()
                .map(CommentResponse::new)
                .toList();
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, @PathVariable Long commentId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        commentService.delete(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> update(@PathVariable Long postId, @PathVariable Long commentId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                       @Valid @RequestBody CommentRequest commentRequest) {
        Long userId = userDetails.getUser().getId();
        commentService.update(commentId, userId, commentRequest.getContent());
        return ResponseEntity.ok().build();
    }

}
