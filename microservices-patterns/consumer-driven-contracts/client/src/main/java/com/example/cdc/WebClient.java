package com.example.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class WebClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public WebClient(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public IsSwearWord check(String input) {
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity(UriComponentsBuilder.fromHttpUrl("http://localhost:8080/checkprofanity")
                        .queryParam("text", input)
                        .build().toUri(),
                        String.class
                );

        try {
            return mapper.readValue(responseEntity.getBody(), IsSwearWord.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
