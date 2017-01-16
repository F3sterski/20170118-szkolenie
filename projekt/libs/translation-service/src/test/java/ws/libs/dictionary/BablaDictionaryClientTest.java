package ws.libs.dictionary;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class BablaDictionaryClientTest {

    BablaDictionaryClient client;

    @Before
    public void setup() {
        URL resource = getClass().getResource("/babla-translation-house.html");
        String s = resource.toExternalForm().replace("house", "%s");
        client = new BablaDictionaryClient(s);
    }

    @Test
    public void should_return_empty_stream_for_non_matching_pattern() {
        final String input = "qwerty";
        final Pattern pattern = Pattern.compile("b{2,}");

        Stream<String> result = client.genericMatcher.apply(input, pattern);

        assertThat(result.count(), equalTo(0l));
    }

    @Test
    public void should_return_first_match() {
        final String input = "aaaabccccddddbbbb";
        final Pattern pattern = Pattern.compile("b{2,}");

        List<String> result = client.genericMatcher.apply(input, pattern).collect(Collectors.toList());

        assertThat(result.isEmpty(), equalTo(false));
        assertThat(result.get(0), equalTo("bbbb"));
    }

    @Test
    public void should_return_polish_word() {
        final String input = "<div class=\"span6 result-right row-fluid\"><a href=\"javascript:babSpeakIt('polski',70442);\"  class=\"tooltipLink\" rel=\"tooltip\" data-original-title=\"Posłuchaj\"><i class=\"icon-volume-up\"></i></a> <a class=\"result-link\" href=\"/slownik/polski-angielski/prosz%C4%99\">proszę</a><span> {<abbr title=\"przysłówek\">przysł.</abbr>}</span></div>";

        List<String> result = client.polishWordPattern.apply(input).collect(Collectors.toList());

        assertThat(result.isEmpty(), equalTo(false));
        assertThat(result.get(0), equalTo("proszę"));
    }

    @Test
    public void should_return_english_word() {
        final String input = "<div class=\"span6 result-left\"><p><a href=\"javascript:babSpeakIt('angielski',138628);\"  class=\"tooltipLink\" rel=\"tooltip\" data-original-title=\"Posłuchaj\"><i class=\"icon-volume-up\"></i></a> <a class=\"result-link\" href=\"/slownik/angielski-polski/please\"><strong>please</strong></a><span> {<abbr title=\"przysłówek\">przysł.</abbr>}</span></p></div>";

        List<String> result = client.englishWordPattern.apply(input).collect(Collectors.toList());

        assertThat(result.isEmpty(), equalTo(false));
        assertThat(result.get(0), equalTo("please"));
    }

    @Test
    public void should_return_one_translation_from_stub() {
        Optional<DictionaryWord> house = client.firstTranslationFor("house");

        assertThat(house.isPresent(), equalTo(true));
        assertThat(house.get().englishWord, equalTo("house"));
        assertThat(house.get().polishWord, equalTo("dom"));
    }

    @Test
    public void should_return_all_translations_from_stub() {
        List<DictionaryWord> translations = client.allTranslationsFor("house");

        assertThat(translations.isEmpty(), equalTo(false));
        assertThat(translations.size(), equalTo(8));
    }

    @Test
    public void should_return_empty_collection_for_non_existing_translation() {
        List<DictionaryWord> translations = client.allTranslationsFor("word-that-doesnt-exists");

        assertThat(translations.isEmpty(), equalTo(true));
    }

    @Test
    public void should_return_optional_for_non_existing_first_translation() {
        Optional<DictionaryWord> translation = client.firstTranslationFor("word-that-doesnt-exists");

        assertThat(translation.isPresent(), equalTo(false));
    }

}