package com.example.blog_project.like;

import com.example.blog_project.post.Post;
import com.example.blog_project.post.PostRepository;
import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService
{
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //좋아요 토글
    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));

        Optional<Like> exsistingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if(exsistingLike.isEmpty()) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
            return true;
        } else {
            likeRepository.delete(exsistingLike.get());
            return false;
        }
    }
}
