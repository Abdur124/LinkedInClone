package com.springboot.clone.linkedin.posts_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostsConfig {

    @Value("${kafka.topic.name.postcreated}")
    private String postCreatedTopic;

    @Value("${kafka.topic.name.postliked}")
    private String postLikedTopic;

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public NewTopic postCreateTopic() {
        return new NewTopic(postCreatedTopic, 3, (short) 1);
    }

    public NewTopic postLikedTopic() {
        return new NewTopic(postLikedTopic, 3, (short) 1);
    }
}
