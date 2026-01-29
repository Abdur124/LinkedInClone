package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.clients.ConnectionsClient;
import com.springboot.clone.linkedin.clients.UserClient;
import com.springboot.clone.linkedin.dto.PersonDto;
import com.springboot.clone.linkedin.entities.Notification;
import com.springboot.clone.linkedin.events.PostCreateEvent;
import com.springboot.clone.linkedin.events.PostLikeEvent;
import com.springboot.clone.linkedin.repos.NotificationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsConsumer {

    private final ConnectionsClient connectionsClient;
    private final UserClient userClient;
    private final NotificationsRepository notificationsRepository;

    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(topics = "post-created-topic", groupId = "posts-group")
    public void consumeCreatedPosts(PostCreateEvent event) {

        log.info("Received Post: {}", event.getContent());
        Long userId = event.getUserId();
        String username = userClient.getUserName(userId);
        List<PersonDto> connections = connectionsClient.getFirstDegreeConnectionsByUserId(username);

        for (PersonDto connection : connections) {
            sendNotifications(String.valueOf(connection.getUserId()), "Your connection "+event.getUserId()+" has created" +
                    " a post, Check it out");
        }
    }

    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(topics = "post-liked-topic", groupId = "posts-group")
    public void consumeLikedPosts(PostLikeEvent event) {

        log.info("Liked Post: {}", event.getPostId());
        String message = String.format("Your post, %d has been liked by %d", event.getPostId(),
                event.getLikedByUserId());
        sendNotifications(String.valueOf(event.getCreatorId()), message);
    }

    public void sendNotifications(String userId, String content) {

            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setMessage(content);
            //optional: Can send out mail notifications if needed
            notificationsRepository.save(notification);
    }
}
