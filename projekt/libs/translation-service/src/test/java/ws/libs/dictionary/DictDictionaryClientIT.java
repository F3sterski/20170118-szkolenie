package ws.libs.dictionary;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class DictDictionaryClientIT {

    DictDictionaryClient client = new DictDictionaryClient();

    @Test
    public void should_return_translations_for_house() {
        List<DictionaryWord> house = client.allTranslationsFor("house");

        assertThat(house.size(), equalTo(24));
    }

    @Test
    public void should_return_empty_list_for_non_existing_word() {
        List<DictionaryWord> house = client.allTranslationsFor("yada-yada");

        assertThat(house.isEmpty(), equalTo(true));
    }

}