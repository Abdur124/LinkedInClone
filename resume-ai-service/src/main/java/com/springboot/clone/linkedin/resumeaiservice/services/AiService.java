package com.springboot.clone.linkedin.resumeaiservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

    public String enhanceResume(String prompt) {

        //return chatClient.prompt().user(prompt).call().content();
        return chatClient.prompt().user(prompt).call().content();
    }

    public String extractKeywords(String prompt) {

        return chatClient.prompt().user(prompt).call().content();
    }
}
