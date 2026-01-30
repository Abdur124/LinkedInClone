package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.entities.Message;

import java.util.List;

public interface MessageService {

    public Message sendMessage(Long conversationId, Long senderId, String content);

    List<Message> getMessages(Long conversationId, Long userId);
}
