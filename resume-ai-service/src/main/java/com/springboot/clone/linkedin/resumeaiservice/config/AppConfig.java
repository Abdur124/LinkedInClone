package com.springboot.clone.linkedin.resumeaiservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.clone.linkedin.resumeaiservice.dtos.QuantifyRequest;
import com.springboot.clone.linkedin.resumeaiservice.models.ResumeTools;
import com.springboot.clone.linkedin.resumeaiservice.services.RagService;
import io.qdrant.client.QdrantClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    CommandLineRunner runner(RagService ragService) {
        return args -> {
            ragService.saveData(List.of(
                    "Reduced API latency by 40% using Redis caching",
                    "Handled millions of financial transactions in low-latency systems",
                    "Designed scalable microservices using Kafka and Spring Boot",
                    "Improved system performance by optimizing database queries",
                    "Built event-driven systems with asynchronous processing",

                    "Microservices architecture enables scalable distributed systems",
                    "Redis is used for caching and low latency applications",
                    "Kafka supports event-driven architecture",
                    "Spring Boot is used to build REST APIs",
                    "SQL databases like MySQL and PostgreSQL are used in backend systems",
                    "Multithreading improves concurrency in Java applications",
                    "Performance tuning improves system scalability and latency"
            ));
            log.info("Sample data inserted successfully");
        };
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 ResumeTools resumeTools) {

        return builder
                .defaultTools(resumeTools)
                .build();
    }

    @Bean
    public FunctionToolCallback quantifyTool(ResumeTools tools) {

        return FunctionToolCallback.builder("quantify", tools::quantify)
                .description("Adds measurable business impact to resume bullets")
                .inputType(QuantifyRequest.class)
                .build();
    }

    @Bean
    public FunctionToolCallback enhanceTool(ResumeTools tools) {

        return FunctionToolCallback.builder("enhanceBullet", tools::enhanceBullet)
                .description("Enhances resume bullet points into ATS-friendly statements")
                .inputType(QuantifyRequest.class)
                .build();
    }
}
