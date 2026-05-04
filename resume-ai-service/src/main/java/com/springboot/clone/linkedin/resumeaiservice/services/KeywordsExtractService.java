package com.springboot.clone.linkedin.resumeaiservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeywordsExtractService {

    private final AiService aiService;
    private final ObjectMapper objectMapper;

    public List<String> extractKeywordsFromJd(String jdText, List<String> expandedKeywords) {

        String prompt = buildPrompt(jdText, expandedKeywords.toString());

        String response = aiService.extractKeywords(prompt); // OpenAI call

        return parseResponse(response);
    }

    private String buildPrompt(String jdText, String context) {

        PromptTemplate template = new PromptTemplate("""
                Extract the most important technical skills and keywords from the job description.
                
                You are given:
                1. A Job Description
                2. Retrieved Context (from a knowledge base)
                
                Instructions:
                - Identify relevant technical skills from the Job Description
                - Use the Context only to:
                  - Prefer standard industry skill keywords over long descriptive phrases.
                  - add closely related technologies
                - Do NOT include irrelevant or generic words from the context
                - Prefer industry-standard skill terms
                
                Rules:
                - Include programming languages, frameworks, tools, and backend concepts
                - Include synonyms or related concepts where appropriate
                - Avoid duplicates
                - Keep output concise (10–20 keywords max)
                - Return ONLY a valid JSON array of strings
                - Ensure all critical technical concepts explicitly mentioned in the Job Description are included.
                
                Job Description:
                {JD_TEXT}
                
                Context:
                {CONTEXT}
                """);

        Map<String, Object> templateMap = Map.of("JD_TEXT", jdText, "CONTEXT", context);

        return template.create(templateMap).getContents();
    }

    private List<String> parseResponse(String response) {

        try {
            // Step 1: Extract JSON array part
            int start = response.indexOf("[");
            int end = response.lastIndexOf("]");

            if (start != -1 && end != -1 && end > start) {
                String jsonArray = response.substring(start, end + 1);

                // Step 2: Parse JSON
                return objectMapper.readValue(
                        jsonArray,
                        new TypeReference<List<String>>() {}
                );
            }

        } catch (Exception e) {
            System.out.println("Failed to parse JSON response: " + e.getMessage());
        }

        // Step 3: Fallback (very important)
        return fallbackParse(response);
    }

    private List<String> fallbackParse(String response) {

        List<String> keywords = new ArrayList<>();

        String[] tokens = response.split(",");

        for (String token : tokens) {
            String cleaned = token.trim()
                    .replaceAll("[^a-zA-Z0-9\\s]", "")
                    .toLowerCase();

            if (!cleaned.isEmpty()) {
                keywords.add(cleaned);
            }
        }

        return keywords;
    }
}
