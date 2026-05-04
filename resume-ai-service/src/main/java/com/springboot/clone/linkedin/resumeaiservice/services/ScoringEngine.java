package com.springboot.clone.linkedin.resumeaiservice.services;

import com.springboot.clone.linkedin.resumeaiservice.dtos.AtsResponse;
import com.springboot.clone.linkedin.resumeaiservice.dtos.ScoreBreakdown;
import com.springboot.clone.linkedin.resumeaiservice.models.BulletScore;
import com.springboot.clone.linkedin.resumeaiservice.models.JDData;
import com.springboot.clone.linkedin.resumeaiservice.models.Resume;
import com.springboot.clone.linkedin.resumeaiservice.models.ResumeData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScoringEngine {

    public AtsResponse compute(JDData jd, ResumeData resume) {

        double keywordScore = computeKeywordMatch(jd, resume);
        double structureScore = computeStructureScore(resume);
        double bulletScore = computeBulletScore(resume);
        double readabilityScore = computeReadability(resume);
        double roleScore = computeRoleAlignment(jd, resume);

        double overallScore =
                0.35 * keywordScore +
                        0.20 * structureScore +
                        0.20 * bulletScore +
                        0.15 * readabilityScore +
                        0.10 * roleScore;

        List<String> missingKeywords = getMissingKeywords(jd, resume);

        List<String> insights = generateInsights(
                keywordScore, structureScore, bulletScore, resume
        );

        List<String> recommendations = generateRecommendations(
                keywordScore, structureScore, bulletScore, resume, missingKeywords
        );

        return AtsResponse.builder()
                .overallScore(round(overallScore))
                .breakdown(ScoreBreakdown.builder()
                        .keywordMatch(round(keywordScore))
                        .sectionStructure(round(structureScore))
                        .bulletQuality(round(bulletScore))
                        .readability(round(readabilityScore))
                        .roleAlignment(round(roleScore))
                        .build())
                .missingKeywords(missingKeywords)
                .insights(insights)
                .recommendations(recommendations)
                .build();
    }

    private double computeKeywordMatch(JDData jd, ResumeData resume) {

        List<String> jdKeywords = jd.getKeywords();
        List<String> resumeSkills = resume.getSkills();

        int matched = 0;

        for (String jdKey : jdKeywords) {
            for (String skill : resumeSkills) {
                if (isMatch(jdKey, skill)) {
                    matched++;
                    break;
                }
            }
        }

        return jdKeywords.isEmpty() ? 0 : (matched * 100.0) / jdKeywords.size();
    }

    private boolean isMatch(String jd, String skill) {
        jd = jd.toLowerCase();
        skill = skill.toLowerCase();

        return jd.contains(skill) || skill.contains(jd);
    }

    private double computeStructureScore(ResumeData resume) {

        int score = 0;

        if (resume.isHasExperience()) score += 40;
        if (resume.isHasSkills()) score += 30;
        if (resume.isHasEducation()) score += 30;

        return score;
    }

    private double computeBulletScore(ResumeData resume) {

        List<BulletScore> bullets = resume.getBulletScores();

        if (bullets == null || bullets.isEmpty()) return 50; // neutral

        double total = 0;

        for (BulletScore b : bullets) {
            double score = b.getImpactScore();

            if (b.isHasMetrics()) score += 10;
            if (b.isHasActionVerb()) score += 5;

            total += Math.min(score, 100);
        }

        return total / bullets.size();
    }

    private double computeReadability(ResumeData resume) {

        // For now: assume decent readability
        // Later: sentence length, symbols, formatting etc.

        return 80;
    }

    private double computeRoleAlignment(JDData jd, ResumeData resume) {

        // MVP: keep neutral
        return 70;
    }

    private List<String> getMissingKeywords(JDData jd, ResumeData resume) {

        List<String> missing = new ArrayList<>();

        for (String jdKey : jd.getKeywords()) {

            boolean found = resume.getSkills().stream()
                    .anyMatch(skill -> isMatch(jdKey, skill));

            if (!found) {
                missing.add(formatKeyword(jdKey));
            }
        }

        return missing;
    }

    private String formatKeyword(String keyword) {

        switch (keyword.toLowerCase()) {
            case "rest": return "REST APIs";
            case "microservices": return "Microservices Architecture";
            default: return keyword.substring(0, 1).toUpperCase() + keyword.substring(1);
        }
    }

    private List<String> generateInsights(double keywordScore,
                                          double structureScore,
                                          double bulletScore,
                                          ResumeData resume) {

        List<String> insights = new ArrayList<>();

        long weakBullets = resume.getBulletScores().stream()
                .filter(b -> b.getImpactScore() < 65)
                .count();

        long strongBullets = resume.getBulletScores().stream()
                .filter(b -> b.getImpactScore() >= 80)
                .count();

        if (keywordScore > 75) {
            insights.add("Strong alignment with required technical skills");
        } else {
            insights.add("Missing key backend and API-related skills from job description");
        }

        if (structureScore >= 90) {
            insights.add("Resume is well structured with all essential sections");
        }

        if (strongBullets > 0) {
            insights.add("Some experience points demonstrate strong measurable impact");
        }

        if (weakBullets > 0) {
            insights.add(weakBullets + " bullet points lack quantified achievements");
        }

        return insights;
    }

    private List<String> generateRecommendations(
            double keywordScore,
            double structureScore,
            double bulletScore,
            ResumeData resume,
            List<String> missingKeywords
    ) {

        List<String> recs = new ArrayList<>();

        if (keywordScore < 70) {
            recs.add("Include missing keywords: " + String.join(", ", missingKeywords));
        }

        if (structureScore < 80) {
            recs.add("Ensure resume includes Experience, Skills, and Education sections");
        }

        if (bulletScore < 70) {
            long noMetrics = resume.getBulletScores().stream()
                    .filter(b -> !b.isHasMetrics())
                    .count();

            if (noMetrics > 0) {
                recs.add("Add measurable impact (%, scale, volume) to " + noMetrics + " bullet points");
            }
        }

        return recs;
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
