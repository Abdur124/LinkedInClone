package com.springboot.clone.linkedin.services;

import com.springboot.clone.linkedin.auth.UserContextHolder;
import com.springboot.clone.linkedin.clients.UserClient;
import com.springboot.clone.linkedin.dto.PersonDto;
import com.springboot.clone.linkedin.entities.Person;
import com.springboot.clone.linkedin.events.AcceptConnectionRequestEvent;
import com.springboot.clone.linkedin.events.SendConnectionRequestEvent;
import com.springboot.clone.linkedin.repos.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final UserClient userClient;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;

    public Person createPerson(PersonDto personDto) {
        Person person = new Person();
        person.setName(personDto.getName());
        person.setRole(personDto.getRole());
        return personRepository.save(person);
    }

    public List<Person> findFirstDegreeConnections(String username) {

            return personRepository.findFirstDegreeConnections(username);
    }


    public Boolean sendConnectionRequest(String receiver) {

        Long senderId = UserContextHolder.getCurrentUserId();
        String sender = userClient.getUserName(senderId);

        if(sender.equals(receiver)) {
            throw new RuntimeException("sender and Receiver cannot be the same");
        }

        boolean connectionRequestExists = personRepository.connectionRequestExists(sender, receiver);

        if(connectionRequestExists) {
            throw new RuntimeException("Connection request already exists, cannot send again");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(sender, receiver);

        if(alreadyConnected) {
            throw new RuntimeException("Already connected users, cannot add connection request");
        }

        personRepository.addConnectionRequest(sender, receiver);

        log.info("Successfully sent the connection request for user {} and receiver {}", sender, receiver);

        SendConnectionRequestEvent connectionRequestEvent = SendConnectionRequestEvent.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        sendRequestKafkaTemplate.send("send-connection-request-topic", senderId, connectionRequestEvent);

        log.info("Successfully sent the connection request notification for user {} and receiver {}", sender, receiver);

        return true;
    }


    public Boolean acceptConnectionRequest(String sender) {

        Long receiverId = UserContextHolder.getCurrentUserId();
        String receiver = userClient.getUserName(receiverId);

        boolean connectionRequestExists = personRepository.connectionRequestExists(sender, receiver);

        if(!connectionRequestExists) {
            throw new RuntimeException("No connection request exists to accept");
        }

        personRepository.acceptConnectionRequest(sender, receiver);

        log.info("Successfully accepted connection request for user {} and receiver {}", sender, receiver);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        acceptRequestKafkaTemplate.send("accept-connection-request-topic", receiverId, acceptConnectionRequestEvent);

        return true;
    }

    public Boolean rejectConnectionRequest(String sender) {

        Long receiverId = UserContextHolder.getCurrentUserId();
        String receiver = userClient.getUserName(receiverId);

        boolean connectionRequestExists = personRepository.connectionRequestExists(sender, receiver);

        if(!connectionRequestExists) {
            throw new RuntimeException("No connection request exists to reject");
        }

        personRepository.rejectConnectionRequest(sender, receiver);

        log.info("Successfully rejected connection request for user {} and receiver {}", sender, receiver);

        return true;
    }
}
