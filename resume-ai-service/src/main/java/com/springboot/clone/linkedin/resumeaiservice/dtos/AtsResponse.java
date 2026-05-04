package com.springboot.clone.linkedin.resumeaiservice.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AtsResponse {

    private double overallScore;

    private ScoreBreakdown breakdown;

    private List<String> missingKeywords;

    private List<String> insights;

    private List<String> recommendations;
}
