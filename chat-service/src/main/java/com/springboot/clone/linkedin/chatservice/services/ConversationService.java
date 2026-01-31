package com.springboot.clone.linkedin.chatservice.services;

import com.springboot.clone.linkedin.chatservice.entities.Conversation;

import java.util.List;

public interface ConversationService {

    Conversation getOrCreateDM(Long userId, Long targetUserId);

    List<Conversation> getUserConversations(Long userId);
}
