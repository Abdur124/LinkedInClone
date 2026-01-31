package com.springboot.clone.linkedin.chatservice.repos;

import com.springboot.clone.linkedin.chatservice.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
}
