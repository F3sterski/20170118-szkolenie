package ws.libs.profanity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfanityCheckClientTest {

    @Mock
    ContainsProfanityService containsProfanityService;

    @Mock
    ProfanityEscapeService profanityEscapeService;

    @InjectMocks
    ProfanityCheckClient client;

    @Before
    public void setup() {
        when(containsProfanityService.test("This is shit")).thenReturn(true);
        when(containsProfanityService.test("This is ok")).thenReturn(false);
        when(profanityEscapeService.escape("This is shit")).thenReturn("This is ****");
        when(profanityEscapeService.escape("This is ok")).thenReturn("This is ok");
    }

    @Test
    public void should_return_escaped_query() {
        final String input = "This is shit";

        IsSwearWord isSwearWord = client.profanityCheck(input);
        assertThat(isSwearWord.containsProfanity, equalTo(true));
        assertThat(isSwearWord.input, equalTo("This is shit"));
        assertThat(isSwearWord.output, equalTo("This is ****"));
    }

    @Test
    public void should_not_escape_sentence() {
        final String input = "This is ok";

        IsSwearWord isSwearWord = client.profanityCheck(input);
        assertThat(isSwearWord.containsProfanity, equalTo(false));
        assertThat(isSwearWord.input, equalTo("This is ok"));
        assertThat(isSwearWord.output, isEmptyOrNullString());
    }

}