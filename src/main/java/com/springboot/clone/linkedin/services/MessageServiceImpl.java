package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.entities.Conversation;
import com.springboot.clone.linkedin.entities.Message;
import com.springboot.clone.linkedin.repos.ConversationParticipantRepository;
import com.springboot.clone.linkedin.repos.ConversationRepository;
import com.springboot.clone.linkedin.repos.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Message sendMessage(Long conversationId, Long senderId, String content) {

        boolean isParticipant = participantRepository
                .existsByConversationIdAndUserId(conversationId, senderId);

        if (!isParticipant) {
            throw new IllegalStateException("User not part of conversation");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("conversation not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .senderId(senderId)
                .content(content)
                .createdAt(Instant.now())
                .build();

        messageRepository.save(message);

        conversationRepository.updateLastMessage(conversationId, content, Instant.now());

        return null;
    }

    @Override
    public List<Message> getMessages(Long conversationId, Long userId) {

        boolean isParticipant = participantRepository
                .existsByConversationIdAndUserId(conversationId, userId);

        if (!isParticipant) {
            throw new IllegalStateException("User not part of conversation");
        }

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
