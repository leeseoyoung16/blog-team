package com.example.blog_project.comment;

import com.example.blog_project.post.Post;
import com.example.blog_project.post.PostRepository;
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
public class CommentService
{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long postId, Long userId, String content)
    {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    @Transactional
    public List<Comment> findByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        return commentRepository.findByPost(post);
    }

//    @Transactional
//    public List<Comment> findByUserId(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
//        return commentRepository.findByUser(user);
//    }

    @Transactional
    public void delete(Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));

        Long commentOwnerId = comment.getUser().getId();
        if(!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public void update(Long id, Long userId, String content) {
        Comment comment = commentRepository.findByIdWithUser(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
        if(!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 댓글만 수정할 수 있습니다.");
        }
        comment.setContent(content);
        commentRepository.save(comment);
    }
}
