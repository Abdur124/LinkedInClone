package com.springboot.clone.linkedin.resumeaiservice.services;

import com.springboot.clone.linkedin.resumeaiservice.models.JDData;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JDProcessor {

    private final RagService ragService;
    private final KeywordsExtractService keywordsExtractService;

    private static final List<String> SKILL_DB = List.of(
            "java", "spring", "spring boot", "microservices",
            "mysql", "oracle", "redis", "kafka",
            "aws", "docker", "kubernetes",
            "rest", "api", "hibernate"
    );

    public JDData process(String jdText) {

        String normalizedText = normalize(jdText);

        List<String> keywords = extractKeywords(normalizedText);

        //calls AI for more keywords
        // 2. RAG retrieval
        List<Document> contextDocs = ragService.retrieveData(jdText);

        List<String> expandedKeywords = contextDocs.stream()
                .map(Document::getFormattedContent)
                .flatMap(text -> Arrays.stream(normalize(text).split("\\s+")))
                .filter(token -> token.length() > 3)
                .distinct()
                .toList();

        List<String> finalKeywords = keywordsExtractService.extractKeywordsFromJd(normalizedText, expandedKeywords);

        System.out.println("Base Keywords: " + keywords);
        System.out.println("RAG Keywords: " + expandedKeywords);
        System.out.println("Final Keywords: " + finalKeywords);

        return new JDData(keywords, expandedKeywords);
    }

    private String normalize(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
    }

    private List<String> extractKeywords(String text) {

        Set<String> keywords = new HashSet<>();

        Set<String> tokens = new HashSet<>(Arrays.asList(text.split("\\s+")));

        for (String skill : SKILL_DB) {
            if (tokens.contains(skill)) {
                keywords.add(skill);
            }
        }

        // 2. Add frequent words (basic signal)
        Map<String, Integer> freqMap = buildFrequencyMap(text);

        freqMap.entrySet().stream()
                .filter(e -> e.getValue() > 2) // appears multiple times
                .map(Map.Entry::getKey)
                .filter(this::isMeaningfulWord)
                .forEach(keywords::add);

        return new ArrayList<>(keywords);
    }

    private Map<String, Integer> buildFrequencyMap(String text) {

        Map<String, Integer> map = new HashMap<>();

        String[] tokens = text.split("\\s+");

        for (String token : tokens) {
            if (token.isBlank()) continue;

            map.put(token, map.getOrDefault(token, 0) + 1);
        }

        return map;
    }

    private static final Set<String> STOPWORDS = Set.of(
            "the", "and", "with", "for", "you", "are", "this",
            "that", "will", "have", "has", "from", "your",
            "our", "job", "role", "team", "work", "experience"
    );

    private boolean isMeaningfulWord(String word) {

        return word.length() > 3 && !STOPWORDS.contains(word);
    }
}
