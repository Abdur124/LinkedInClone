package com.springboot.clone.linkedin.chatservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "conversation_participant", uniqueConstraints = @UniqueConstraint(
                columnNames = {"conversation_id", "user_id"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationParticipant {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private Long conversationId;
    private Long userId;
    private Instant joinedAt;
}
