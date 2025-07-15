package com.example.blog_project.comment;

import com.example.blog_project.post.Post;
import com.example.blog_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> findByPost(Post post);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Comment> findByIdWithUser(@Param("id") Long id);
}
