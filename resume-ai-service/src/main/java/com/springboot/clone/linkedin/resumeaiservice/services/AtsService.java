package com.springboot.clone.linkedin.resumeaiservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.clone.linkedin.resumeaiservice.dtos.AtsRequest;
import com.springboot.clone.linkedin.resumeaiservice.dtos.AtsResponse;
import com.springboot.clone.linkedin.resumeaiservice.models.JDData;
import com.springboot.clone.linkedin.resumeaiservice.models.ResumeData;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AtsService {

    @Autowired
    private JDProcessor jdProcessor;

    @Autowired
    private ResumeProcessor resumeProcessor;

    @Autowired
    private ScoringEngine scoringEngine;

    @Autowired
    private AiService aiService;

    @Autowired
    private ObjectMapper objectMapper;

    public AtsResponse computeScore(AtsRequest atsRequest) {

        JDData jdData = jdProcessor.process(atsRequest.getJobDescription());

        ResumeData resumeData = resumeProcessor.process(atsRequest.getResumeText());

        return scoringEngine.compute(jdData, resumeData);
    }

}
