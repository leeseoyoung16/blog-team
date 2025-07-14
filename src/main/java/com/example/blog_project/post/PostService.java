package com.example.blog_project.post;

import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService
{
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long userId, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        postRepository.save(post);
    }

    @Transactional
    public List<Post> findAll()
    {
        return postRepository.findAll();
    }

    @Transactional
    public List<Post> findByUserId(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        return postRepository.findByUser(user);
    }

    @Transactional
    public Post findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        return post;
    }

    @Transactional
    public void delete(Long id, Long userId)
    {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        if(!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 게시글만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }

    @Transactional
    public void update(Long id, String title, String content, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        if(!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 게시글만 수정할 수 있습니다.");
        }
        post.setTitle(title);
        post.setContent(content);
    }
}
