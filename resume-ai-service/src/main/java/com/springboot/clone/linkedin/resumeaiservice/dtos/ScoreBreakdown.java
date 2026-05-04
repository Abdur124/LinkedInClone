package com.springboot.clone.linkedin.resumeaiservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreBreakdown {

    private double keywordMatch;
    private double sectionStructure;
    private double bulletQuality;
    private double readability;
    private double roleAlignment;
}
