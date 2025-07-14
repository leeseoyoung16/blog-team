package com.example.blog_project.post;

import com.example.blog_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>
{
    List<Post> findByUser(User user);
}
