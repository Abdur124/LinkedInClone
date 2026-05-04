package com.springboot.clone.linkedin.resumeaiservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulletScore {

    private String bullet;

    private boolean hasActionVerb;
    private boolean hasMetrics;

    private double impactScore;

    private String suggestion;
}
