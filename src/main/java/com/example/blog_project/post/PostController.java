package com.example.blog_project.post;

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
@RequestMapping("/posts")
public class PostController
{
    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PostRequest postRequest,
                                       @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        Long userId = userDetails.getUser().getId();
        postService.create(userId, postRequest.getTitle(), postRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.findAll();
        List<PostResponse> postResponse = posts.stream()
                .map(PostResponse::new)
                .toList();
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        List<Post> posts = postService.findByUserId(user.getId());
        List<PostResponse> postResponse = posts.stream()
                .map(PostResponse::new)
                .toList();
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        return ResponseEntity.ok(new PostResponse(post));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        postService.delete(postId, userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> update(@PathVariable Long postId, @Valid @RequestBody PostRequest postRequest,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.update(postId, postRequest.getTitle(), postRequest.getContent(), userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }
}
