package ws.client;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoogleTranslateTest {

    final String key = "AIzaSyB" +
            "dTwqr8P" +

            "h4xyb92" +

            "d9fYNz2" +

            "CrUT8Au" +
            "oMhY";

    @Test
    public void should_translate_with_google() {
        final String input = "Hi my name is John";
        final String output = "Cześć, nazywam się John";

        GoogleTranslate t = new GoogleTranslate(key);
        assertThat(t.translationFor(input), CoreMatchers.equalTo(output));
    }

}