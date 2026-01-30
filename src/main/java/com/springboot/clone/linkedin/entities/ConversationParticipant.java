package com.springboot.clone.linkedin.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "conversation_participant",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"conversation_id", "user_id"}
        )
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;
    private Long userId;

    private Instant joinedAt;
}
