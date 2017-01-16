package ws.libs.profanity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class ProfanityCheckClientIT {

    ContainsProfanityService containsProfanityService = new ContainsProfanityServiceImpl();
    ProfanityEscapeService profanityEscapeService = new ProfanityEscapeServiceImpl();

    @Test
    public void profanityCheck() throws Exception {
        //given
        final String input = "This is shit";
        ProfanityCheckClient client = new ProfanityCheckClient(containsProfanityService, profanityEscapeService);

        //when
        IsSwearWord isSwearWord = client.profanityCheck(input);

        assertThat(isSwearWord.containsProfanity, equalTo(true));
        assertThat(isSwearWord.output, equalTo("This is ****"));
    }

}