package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.entities.Conversation;
import com.springboot.clone.linkedin.entities.ConversationParticipant;
import com.springboot.clone.linkedin.repos.ConversationParticipantRepository;
import com.springboot.clone.linkedin.repos.ConversationRepository;
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

    @Override
    public Conversation getOrCreateDM(Long userId, Long targetUserId) {

        if(userId.equals(targetUserId)){
            throw new RuntimeException("user id cannot be the same as the target user id");
        }

        String conversationKey = generateConversationKey(userId, targetUserId);

        return conversationRepository.findByConversationKey(conversationKey)
                .orElseGet(() -> createDMConversation(conversationKey, userId, targetUserId));
    }

    private Conversation createDMConversation(String conversationKey, Long userId, Long targetUserId) {
        Conversation conversation = Conversation.builder()
                .conversationKey(conversationKey)
                .user1Id(userId).user2Id(targetUserId)
                .createdAt(Instant.now()).updatedAt(Instant.now())
                .build();

        conversation = conversationRepository.save(conversation);

        participantRepository.saveAll(List.of(createParticipant(conversation.getId(), userId),
                createParticipant(conversation.getId(), targetUserId)));

        return conversation;
    }

    private ConversationParticipant createParticipant(Long conversationId, Long userId) {

        return ConversationParticipant.builder()
                .conversationId(conversationId)
                .userId(userId)
                .joinedAt(Instant.now())
                .build();

    }

    @Override
    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findAllByUser1Id(userId);
    }

    private String generateConversationKey(Long userId1, Long userId2) {

        return "DM_" + Math.min(userId1, userId2) + "_" + Math.max(userId1, userId2);
    }
}
