package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.clients.UserClient;
import com.springboot.clone.linkedin.entities.Notification;
import com.springboot.clone.linkedin.events.AcceptConnectionRequestEvent;
import com.springboot.clone.linkedin.events.SendConnectionRequestEvent;
import com.springboot.clone.linkedin.repos.NotificationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsConsumer {

    private final NotificationsRepository notificationsRepository;
    private final UserClient userClient;

    @KafkaListener(topics = "send-connection-request-topic", groupId = "connections-group")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("handle connections: handleSendConnectionRequest: {}", sendConnectionRequestEvent);
        String message =
                "You have receiver a connection request from user %s"+sendConnectionRequestEvent.getSender();
        sendNotifications(sendConnectionRequestEvent.getReceiver(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic", groupId = "connections-group")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("handle connections: handleAcceptConnectionRequest: {}", acceptConnectionRequestEvent);
        String message =
                "Your connection request has been accepted by the user %s"+acceptConnectionRequestEvent.getReceiver();
        sendNotifications(acceptConnectionRequestEvent.getSender(), message);
    }

    private void sendNotifications(String user, String content) {

        Notification notification = new Notification();
        notification.setUserId(user);
        notification.setMessage(content);
        //optional: Can send out mail notifications if needed
        notificationsRepository.save(notification);
    }
}
