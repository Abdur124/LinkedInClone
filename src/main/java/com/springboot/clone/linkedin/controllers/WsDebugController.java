package com.springboot.clone.linkedin.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WsDebugController {

    @MessageMapping("/ping")
    public void ping(String msg) {
        log.error("🔥🔥🔥 PING RECEIVED: {}", msg);
    }
}
