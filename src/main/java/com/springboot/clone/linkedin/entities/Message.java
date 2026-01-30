package com.springboot.clone.linkedin.entities;

import com.springboot.clone.linkedin.entities.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "message")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    private Long senderId;
    private Long receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private MessageStatus messageStatus;

    private Instant createdAt;
}
