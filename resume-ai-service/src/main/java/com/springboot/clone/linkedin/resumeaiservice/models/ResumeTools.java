package com.springboot.clone.linkedin.resumeaiservice.models;

import com.springboot.clone.linkedin.resumeaiservice.dtos.QuantifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ResumeTools {

    @Tool(name = "quantify_bullet",
            description = "Add measurable impact to a resume bullet")
    public String quantify(QuantifyRequest request) {

        String bullet = request.getBullet();

        if (bullet.toLowerCase().contains("performance")) {
            return bullet + " resulting in ~30% improvement";
        }
        if (bullet.toLowerCase().contains("latency")) {
            return bullet + " reducing latency by ~40%";
        }
        return bullet + " with measurable impact";
    }

    @Tool(name = "enhance_bullet",
            description = "Enhance a resume bullet to be more impactful")
    public String enhanceBullet(QuantifyRequest request) {

        String bullet = request.getBullet();
        return "Designed and implemented " + bullet + " in a scalable and production-ready manner";
    }
}
