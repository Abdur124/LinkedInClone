package com.springboot.clone.linkedin.clients;


import com.springboot.clone.linkedin.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/{username}/firstConnections")
    public List<PersonDto> getFirstDegreeConnectionsByUserId(@PathVariable String username);
}
