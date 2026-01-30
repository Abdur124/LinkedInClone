package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.entities.Conversation;

import java.util.List;

public interface ConversationService {

    public Conversation getOrCreateDM(Long userId, Long targetUserId);

    List<Conversation> getUserConversations(Long userId);
}
