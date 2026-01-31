package com.springboot.clone.linkedin.chatservice.repos;

import com.springboot.clone.linkedin.chatservice.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByConversationKey(String conversationKey);

    List<Conversation> findAllByUser1Id(Long userId);

    @Modifying
    @Query("UPDATE Conversation c SET c.lastMessage = :lastMessage, c.updatedAt = :updatedAt WHERE c.id = :conversationId")
    void updateLastMessage(@Param("conversationId") Long conversationId, @Param("lastMessage") String lastMessage, @Param("updatedAt") Instant updatedAt);
}
