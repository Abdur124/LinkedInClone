package com.springboot.clone.linkedin.controllers;

import com.springboot.clone.linkedin.dto.PersonDto;
import com.springboot.clone.linkedin.entities.Person;
import com.springboot.clone.linkedin.services.ConnectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @PostMapping("/addPerson")
    public ResponseEntity<Person> createPerson(@RequestBody PersonDto personDto) {
        Person person = connectionsService.createPerson(personDto);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @GetMapping("/{username}/firstConnections")
    public ResponseEntity<List<Person>> getFirstDegreeConnectionsByUserId(@PathVariable String username) {
        List<Person> firstDegreeConnections = connectionsService.findFirstDegreeConnections(username);
        return new ResponseEntity<>(firstDegreeConnections, HttpStatus.OK);
    }

    @PostMapping("/request/{user}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable String user) {
        return ResponseEntity.ok(connectionsService.sendConnectionRequest(user));
    }

    @PostMapping("/accept/{user}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable String user) {
        return ResponseEntity.ok(connectionsService.acceptConnectionRequest(user));
    }

    @PostMapping("/reject/{user}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable String user) {
        return ResponseEntity.ok(connectionsService.rejectConnectionRequest(user));
    }
}
