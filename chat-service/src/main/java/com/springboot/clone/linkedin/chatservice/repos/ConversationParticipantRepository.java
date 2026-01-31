package com.springboot.clone.linkedin.chatservice.repos;

import com.springboot.clone.linkedin.chatservice.entities.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {

    List<ConversationParticipantRepository> findByUserId(Long userId);

    boolean existsByConversationIdAndUserId(Long conversationId, Long senderId);
}
