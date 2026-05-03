package com.springboot.clone.linkedin.resumeaiservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.clone.linkedin.resumeaiservice.models.Resume;
import com.springboot.clone.linkedin.resumeaiservice.services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/generate")
    public Resume createResume(@RequestBody Resume resume) {
        try {
            return resumeService.generateResume(resume);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/enhance")
    public Resume enhanceResume(@RequestBody Resume resume) {
        return resumeService.enhanceResume(resume);
    }
}
