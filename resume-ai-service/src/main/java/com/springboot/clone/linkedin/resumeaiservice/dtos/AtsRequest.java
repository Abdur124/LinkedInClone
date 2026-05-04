package com.springboot.clone.linkedin.resumeaiservice.dtos;

import lombok.Data;

@Data
public class AtsRequest {

    private String resumeText;
    private String jobDescription;
    private String targetRole;
    private String experienceLevel;
}
