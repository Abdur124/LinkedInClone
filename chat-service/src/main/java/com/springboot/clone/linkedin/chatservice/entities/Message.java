package com.springboot.clone.linkedin.chatservice.entities;

import com.springboot.clone.linkedin.chatservice.entities.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
