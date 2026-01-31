package com.springboot.clone.linkedin.chatservice.services;

import com.springboot.clone.linkedin.chatservice.entities.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(Long conversationId, Long senderId, String content);

    List<Message> getMessages(Long conversationId, Long userId);
}
