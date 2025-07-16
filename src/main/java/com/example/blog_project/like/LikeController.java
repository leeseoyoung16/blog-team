package com.example.blog_project.like;

import com.example.blog_project.CustomUserDetails;
import com.example.blog_project.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController
{
    private final LikeService likeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<String> toggleLike(@PathVariable Long postId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        boolean liked = likeService.toggleLike(postId,userId);
        return ResponseEntity.ok(liked ? "좋아요 완료" : "좋아요 취소");
    }
}
