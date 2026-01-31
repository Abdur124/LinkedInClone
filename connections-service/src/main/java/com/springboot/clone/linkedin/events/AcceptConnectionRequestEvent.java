package com.springboot.clone.linkedin.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcceptConnectionRequestEvent {

    private String sender;
    private String receiver;
}
