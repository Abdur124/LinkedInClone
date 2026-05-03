package com.springboot.clone.linkedin.resumeaiservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.clone.linkedin.resumeaiservice.models.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final AiService aiService;
    private final ObjectMapper objectMapper;
    private final RagService ragService;

    public Resume generateResume(Resume request) throws JsonProcessingException {

        String prompt = buildPrompt(request);

        String response = aiService.enhanceResume(prompt);

        String cleanJson = extractJson(response);

        log.info("Response from AI agent is: {}", cleanJson);
        try {
            return objectMapper.readValue(cleanJson, Resume.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Resume enhanceResume(Resume request) {

        String prompt = constructPromptForRAG(request);

        String response = aiService.enhanceResume(prompt);

        String cleanJson = extractJson(response);

        log.info("Enhanced Response from AI agent is: {}", cleanJson);
        try {
            return objectMapper.readValue(cleanJson, Resume.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String constructPromptForRAG(Resume request) {

        List<Document> contextDocs = ragService.retrieveData(request.getSummary());

        String context = contextDocs.stream().map(Document::getFormattedContent).collect(Collectors.joining("\n"));

        PromptTemplate template = new PromptTemplate("""
                
                You are a senior recruiter and resume expert.
                
                        Use the CONTEXT to improve the resume.
                        
                        You have access to tools:
                        - quantify → adds measurable impact
                        - enhanceBullet → improves bullet quality
                
                        STRICT RULES:
                        - Return ONLY valid JSON
                        - Use tools when needed
                        - No explanation
                        - No extra text
                        - No markdown
                        - Follow the exact structure
                        - Always complete the JSON
                        
                JSON STRUCTURE:
                Return a JSON matching this structure:
                basics, summary, skills, experience, projects, education
                
                Ensure all fields are present and valid.
                        
                CONTEXT:
                {context}
                
                INPUT RESUME:
                {resume}
                
                TASK:
                Enhance resume using context
                Add measurable impact wherever possible (%, scale, latency, throughput)
                """);

        Map<String, Object> templateMap = Map.of("context", context,
                "resume", request.toString());

        return template.create(templateMap).getContents();
    }

    private String extractJson(String res) {

        int start = res.indexOf("{");
        int end = res.lastIndexOf("}");

        return res.substring(start, end+1);
    }

    private String buildPrompt(Resume request) throws JsonProcessingException {

        return """
        You are a senior recruiter and resume expert.

        Your task is to enhance the given resume.

        IMPORTANT RULES:
        - Return ONLY valid JSON
        - Do NOT add explanations, headings, or extra text
        - Follow the exact structure provided
        - Improve summary, experience bullets, and wording
        - Make it ATS friendly with strong action verbs and quantifiable impact
        - Add measurable impact using numbers (%, scale, latency, throughput)
        - If exact numbers are not provided, estimate realistic values
        - Highlight system scale (high-volume, low-latency, distributed systems)
        - Make each bullet point impactful and results-oriented

        JSON STRUCTURE:
                Return a JSON matching this structure:
                basics, summary, skills, experience, projects, education
                
                Ensure all fields are present and valid.

        INPUT RESUME:
        """ + objectMapper.writeValueAsString(request);
    }
}
