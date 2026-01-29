package com.springboot.clone.linkedin.user_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class AppConfig {

    @Value("${user.access.enabled}")
    private String accessValue;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public String getAccessVariable() {
        return accessValue;
    }

}
