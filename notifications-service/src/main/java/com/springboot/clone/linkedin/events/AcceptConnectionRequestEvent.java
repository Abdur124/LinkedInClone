package com.springboot.clone.linkedin.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptConnectionRequestEvent {

    private String sender;
    private String receiver;
}
