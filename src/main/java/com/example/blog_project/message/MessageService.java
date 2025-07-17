package com.example.blog_project.message;

import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService
{
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    //쪽지 보내기
    public void send(String title, String content, Long senderId, Long receiverId, Long parentMessageId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));

        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);

        //답장일 경우
        if(parentMessageId != null) {
            Message parent = messageRepository.findById(parentMessageId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 메세지가 존재하지 않습니다."));
            message.setParent(parent);
        }
        messageRepository.save(message);
    }

    //쪽지 유저별 전체 조회
    public List<Message> findAllMessagesByUser(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        return messageRepository.findAllByUserInvolved(user);
    }

    // 읽지 않은 쪽지 조회
    public List<Message> findUnreadMessagesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        return messageRepository.findUnreadMessagesByUser(user);
    }
    // 받은 쪽지 조회
    public List<Message> findReceivedMessagesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        return messageRepository.findByReceiver(user);
    }
    // 보낸 쪽지 조회
    public List<Message> findSentMessagesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        return messageRepository.findBySender(user);
    }
    //쪽지 단건 조회
    public Message findById(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메세지가 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));
        if(message.getReceiver().equals(user)) {
            message.setIsRead(true);
            messageRepository.save(message);
        }
        return message;
    }

    //쪽지 삭제
    public void delete(Long messageId, Long userId)
    {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메세지가 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다."));

        if(!message.getSender().equals(user)){
            throw new AccessDeniedException("메세지를 보낸 사람만 삭제할 수 있습니다.");
        }
        messageRepository.delete(message);
    }

}
