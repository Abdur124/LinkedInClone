package com.springboot.clone.linkedin.resumeaiservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagService {

    private final VectorStore vectorStore;

    public void saveData(List<String> texts) {

        List<Document> documents = texts.stream().map(Document::new).toList();
        vectorStore.add(documents);
    }

    public List<Document> retrieveData(String query) {

        return vectorStore.similaritySearch(query);
    }
}
