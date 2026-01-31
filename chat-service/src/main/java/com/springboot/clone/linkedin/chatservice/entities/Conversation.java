package com.springboot.clone.linkedin.chatservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "conversation", uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"})
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user1Id;
    private Long user2Id;
    @Column(unique = true, nullable = false)
    private String conversationKey;
    private String lastMessage;
    private Instant createdAt;
    private Instant updatedAt;
}
