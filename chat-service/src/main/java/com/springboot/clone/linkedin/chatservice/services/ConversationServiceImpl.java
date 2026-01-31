package com.springboot.clone.linkedin.chatservice.services;

import com.springboot.clone.linkedin.chatservice.entities.Conversation;
import com.springboot.clone.linkedin.chatservice.entities.ConversationParticipant;
import com.springboot.clone.linkedin.chatservice.repos.ConversationParticipantRepository;
import com.springboot.clone.linkedin.chatservice.repos.ConversationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;

    @Transactional
    public Conversation getOrCreateDM(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new RuntimeException("user id cannot be the same as the target user id");
        } else {
            String conversationKey = this.generateConversationKey(userId, targetUserId);
            return conversationRepository.findByConversationKey(conversationKey).orElseGet(() -> this.createDMConversation(conversationKey, userId, targetUserId));
        }
    }


    private Conversation createDMConversation(String conversationKey, Long userId, Long targetUserId) {
        Conversation conversation = Conversation.builder().conversationKey(conversationKey).user1Id(userId).user2Id(targetUserId).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        conversation = conversationRepository.save(conversation);
        participantRepository.saveAll(
                List.of(
                        createParticipant(conversation.getId(), userId),
                        createParticipant(conversation.getId(), targetUserId)
                )
        );

        return conversation;
    }

    private ConversationParticipant createParticipant(Long conversationId, Long userId) {
        return ConversationParticipant.builder().conversationId(conversationId).userId(userId).joinedAt(Instant.now()).build();
    }

    public List<Conversation> getUserConversations(Long userId) {
        return this.conversationRepository.findAllByUser1Id(userId);
    }

    private String generateConversationKey(Long userId1, Long userId2) {
        long var10000 = Math.min(userId1, userId2);
        return "DM_" + var10000 + "_" + Math.max(userId1, userId2);
    }
}
