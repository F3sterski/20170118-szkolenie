package com.example.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.text.IsEmptyString;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProfanityClient.class)
@AutoConfigureStubRunner(ids = {"com.example.cdc:profanity-check-service:+:stubs:8080"},
        workOffline = true)
public class WebClientTest {

    TestRestTemplate template = new TestRestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    WebClient client = new WebClient(template.getRestTemplate(), mapper);

    @Test
    public void should_contain_no_profanity() {
        final String sentence = "This sentence is ok";
        IsSwearWord check = client.check(sentence);
        assertThat(check.containsProfanity, equalTo(false));
        assertThat(check.input, equalTo(sentence));
        assertThat(check.output, isEmptyString());
    }

    @Test
    public void should_contain_profanity() {
        final String sentence = "This is shit";
        IsSwearWord check = client.check(sentence);

        assertThat(check.containsProfanity, equalTo(true));
        assertThat(check.input, equalTo(sentence));
        assertThat(check.output, equalTo("This is ****"));

    }


}