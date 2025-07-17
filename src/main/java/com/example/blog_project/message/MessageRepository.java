package com.example.blog_project.message;

import com.example.blog_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>
{
    @Query("SELECT m FROM Message m "
            + "WHERE m.sender = :user OR m.receiver = :user "
            + "order by m.createdAt DESC")
    List<Message> findAllByUserInvolved(User user);

    @Query("SELECT m FROM Message m "
            + "WHERE m.receiver = :user AND m.isRead = false "
            + "order by m.createdAt DESC")
    List<Message> findUnreadMessagesByUser(User user);

    @Query("SELECT m FROM Message m "
            + "WHERE m.sender = :user " + "order by m.createdAt DESC")
    List<Message> findBySender(User user);

    @Query("SELECT m FROM Message m "
            + "WHERE m.receiver = :user " + "order by m.createdAt DESC")
    List<Message> findByReceiver(User user);
}
