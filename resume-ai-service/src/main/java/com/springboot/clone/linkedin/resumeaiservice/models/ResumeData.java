package com.springboot.clone.linkedin.resumeaiservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResumeData {

    private List<String> skills;
    private List<BulletScore> bulletScores;

    private boolean hasExperience;
    private boolean hasSkills;
    private boolean hasEducation;
}
