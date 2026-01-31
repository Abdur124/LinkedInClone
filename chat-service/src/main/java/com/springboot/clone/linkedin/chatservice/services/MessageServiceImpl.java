package com.springboot.clone.linkedin.chatservice.services;

import com.springboot.clone.linkedin.chatservice.entities.Conversation;
import com.springboot.clone.linkedin.chatservice.entities.Message;
import com.springboot.clone.linkedin.chatservice.repos.ConversationParticipantRepository;
import com.springboot.clone.linkedin.chatservice.repos.ConversationRepository;
import com.springboot.clone.linkedin.chatservice.repos.MessageRepository;
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

    @Transactional
    public Message sendMessage(Long conversationId, Long senderId, String content) {
        boolean isParticipant = this.participantRepository.existsByConversationIdAndUserId(conversationId, senderId);
        if (!isParticipant) {
            throw new IllegalStateException("User not part of conversation");
        } else {
            Conversation conversation = (Conversation)this.conversationRepository.findById(conversationId).orElseThrow(() -> new RuntimeException("conversation not found"));
            Message message = Message.builder().conversation(conversation).senderId(senderId).content(content).createdAt(Instant.now()).build();
            this.messageRepository.save(message);
            this.conversationRepository.updateLastMessage(conversationId, content, Instant.now());
            return null;
        }
    }

    public List<Message> getMessages(Long conversationId, Long userId) {
        boolean isParticipant = this.participantRepository.existsByConversationIdAndUserId(conversationId, userId);
        if (!isParticipant) {
            throw new IllegalStateException("User not part of conversation");
        } else {
            return this.messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        }
    }
}
