package com.example.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@Configuration
@ComponentScan
public class ProfanityClient {

    @Autowired
    WebClient client;

    public void run() {
        String str = input();
        while (str != null) {

            System.out.println(client.check(str));

            str = input();
        }
    }

    private String input() {
        System.out.print(">> ");
        return new Scanner(System.in).nextLine();
    }

    public static void main(String... args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ProfanityClient.class);
        ProfanityClient app = context.getBean(ProfanityClient.class);
        app.run();
    }

    @Bean
    RestTemplate rest() {
        return new RestTemplate();
    }

    @Bean
    ObjectMapper mapper() {
        return new ObjectMapper();
    };
}
