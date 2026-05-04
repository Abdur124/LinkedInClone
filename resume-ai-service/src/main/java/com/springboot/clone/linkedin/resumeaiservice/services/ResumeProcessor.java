package com.springboot.clone.linkedin.resumeaiservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.clone.linkedin.resumeaiservice.models.BulletScore;
import com.springboot.clone.linkedin.resumeaiservice.models.ResumeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeProcessor {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    private static final List<String> ACTION_VERBS = List.of(
            "built", "developed", "designed", "implemented",
            "optimized", "created", "led", "improved",
            "resolved", "collaborated", "mentored",
            "contributed", "enhanced", "reduced",
            "increased", "managed", "delivered"
    );

    public ResumeData process(String resumeText) {

        String normalizedText = normalize(resumeText);

        List<String> skills = extractSkills(normalizedText);

        List<String> bullets = extractBullets(resumeText);

        List<BulletScore> bulletScores = analyzeBullets(bullets);

        boolean hasExperience = normalizedText.contains("experience");
        boolean hasSkills = normalizedText.contains("skills");
        boolean hasEducation = normalizedText.contains("education");

        return new ResumeData(skills, bulletScores, hasExperience, hasSkills, hasEducation);
    }

    private List<BulletScore> analyzeBullets(List<String> bullets) {

        return bullets.stream()
                .map(this::analyzeBulletHybrid)
                .toList();
    }

    private BulletScore analyzeBulletHybrid(String bullet) {

            try {
                return analyzeBulletWithAI(bullet);
            } catch (Exception e) {
                return analyzeBulletRuleBased(bullet);
            }
    }

    private BulletScore analyzeBulletWithAI(String bullet) {

        String prompt = """
                You are a strict JSON generator.
                
                Return ONLY a valid JSON object.
                Do NOT include explanations, markdown, or text.
                
                Format EXACTLY like this:
                {
                  "bullet": "...",
                  "hasActionVerb": true/false,
                  "hasMetrics": true/false,
                  "impactScore": number (0-100),
                  "suggestion": "..."
                }
                
                Bullet:
                %s
    """.formatted(bullet);

        try {
            String rawResponse = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

          log.info("AI response for resume processor is: {}", rawResponse);

            String clean = rawResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            return objectMapper.readValue(clean, BulletScore.class);
        } catch (RuntimeException | JsonProcessingException e) {
            throw new RuntimeException("Parsing failed: " , e);
        }
    }

    private String normalize(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
    }

    private static final List<String> SKILL_DB = List.of(
            "java", "spring", "spring boot", "microservices",
            "mysql", "oracle", "redis", "kafka",
            "aws", "docker", "kubernetes",
            "rest", "api", "hibernate"
    );

    private List<String> extractSkills(String text) {

        List<String> skills = new ArrayList<>();

        for (String skill : SKILL_DB) {
            if (text.contains(skill)) {
                skills.add(skill);
            }
        }

        return skills;
    }

    private List<String> extractBullets(String text) {

        List<String> bullets = new ArrayList<>();

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) continue;

            // Case 1: Proper bullets
            if (line.startsWith("-") || line.startsWith("•")) {
                bullets.add(line.substring(1).trim());
            }

            // Case 2: Lines that look like bullet sentences
            else if (line.length() > 50 && startsWithActionVerb(line)) {
                bullets.add(line);
            }
        }

        return bullets;
    }

    private boolean startsWithActionVerb(String line) {

        String normalized = normalize(line);

        for (String verb : ACTION_VERBS) {
            if (normalized.startsWith(verb)) {
                return true;
            }
        }

        return false;
    }

    private BulletScore analyzeBulletRuleBased(String bullet) {

        boolean hasActionVerb = containsActionVerb(bullet);
        boolean hasMetrics = containsMetrics(bullet);

        double score = 0;

        if (hasActionVerb) score += 50;
        if (hasMetrics) score += 50;

        String suggestion = generateSuggestion(hasActionVerb, hasMetrics);

        return BulletScore.builder()
                .bullet(bullet)
                .hasActionVerb(hasActionVerb)
                .hasMetrics(hasMetrics)
                .impactScore(score)
                .suggestion(suggestion)
                .build();

    }

    private boolean containsActionVerb(String bullet) {

        String lower = normalize(bullet.toLowerCase());

        for (String verb : ACTION_VERBS) {
            if (lower.contains(verb)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsMetrics(String bullet) {

        String lower = normalize(bullet);

        return lower.matches(".*\\d+.*"); // contains any number
    }

    private String generateSuggestion(boolean hasActionVerb, boolean hasMetrics) {

        if (!hasActionVerb && !hasMetrics) {
            return "Use strong action verbs and include measurable results";
        }

        if (!hasActionVerb) {
            return "Start bullet with a strong action verb";
        }

        if (!hasMetrics) {
            return "Add measurable impact (%, numbers, scale)";
        }

        return "Strong bullet point";
    }
}
