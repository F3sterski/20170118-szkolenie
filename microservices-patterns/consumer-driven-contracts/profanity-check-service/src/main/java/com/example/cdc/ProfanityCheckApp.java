package com.example.cdc;

import com.esotericsoftware.kryo.util.ObjectMap;
import com.example.cdc.history.Record;
import com.example.cdc.history.RecordsDao;
import com.example.cdc.profanity.IsSwearWord;
import com.example.cdc.profanity.PurgoMalumProfanityClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class ProfanityCheckApp {

    @Autowired
    PurgoMalumProfanityClient client;

    @Autowired
    RecordsDao dao;

    @RequestMapping(value = "/checkprofanity", produces = MediaType.APPLICATION_JSON_VALUE)
    public IsSwearWord check(@RequestParam("text") String input) {
        return client.profanityCheck(input);
    }

    //TODO: implement all history records
    //hints: use ObjectMapper.createObjectNode() to conver record to desired JSON representation
    //       reduce whole list into ArrayNode and wrap it in ResponseEntity (stream().reduce())
    @RequestMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity history(Integer limit) {
        final ObjectMapper mapper = new ObjectMapper();

        System.out.println("dao.getAll() = " + dao.getAll());
        ArrayNode reduce = dao.getAll().stream()
                .map(r -> {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("input", r.input).put("time", r.time.getEpochSecond());

                    return node;
                })
                .limit(limit)
                .reduce(mapper.createArrayNode(), ArrayNode::add, ArrayNode::addAll);
        System.out.println("reduce = " + reduce);
        return new ResponseEntity(reduce, HttpStatus.OK);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProfanityCheckApp.class, args);
    }

}

@Aspect
@Component
class InputPhraseLogger {

    @Autowired
    RecordsDao dao;

    @Before("execution(* com.example.cdc.ProfanityCheckApp.check(..)) && args(input)")
    public void entry(String input) {
        dao.add(input, Instant.now());
    }

}