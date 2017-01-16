package ws.libs.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DictDictionaryClient implements DictionaryClient {

    private static final Logger log = Logger.getLogger(DictDictionaryClient.class.getName());
    private final String urlString;

    public DictDictionaryClient() {
        this.urlString = "http://www.dict.pl/dict?word=%s&words=&lang=PL";
    }

    public DictDictionaryClient(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public Optional<DictionaryWord> firstTranslationFor(String wordToFind) {
        List<DictionaryWord> foundWords = allTranslationsFor(wordToFind);
        return foundWords.stream().findFirst();
    }

    @Override
    public List<DictionaryWord> allTranslationsFor(String wordToFind) {
        log.info("Calling: " + url(wordToFind));
        try (InputStream io = new URL(url(wordToFind)).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(io))) {

            List<String> collect = reader.lines()
                .flatMap(DictDictionaryClient::match) ////JDK9 will has Optional.stream() so we would be able to do flatMap here
                .collect(Collectors.toList());

            return IntStream.range(0, collect.size())
                    .filter(i -> i%2==1)
                    .mapToObj(i -> new DictionaryWord(collect.get(i),collect.get(i-1)))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.warning(String.format("Couldn't process the stream %s. Empty list", url(wordToFind)));
            return Collections.emptyList();
        }
    }

    private static Stream<String> match(String s) {
        Pattern pattern = Pattern
                .compile(".*<a href=\"dict\\?words?=(.*)&lang.*");

        Matcher m = pattern.matcher(s);
        if (m.find()) {
            return Stream.of(m.group(m.groupCount()));
        } else {
            return Stream.empty();
        }
    }

    private String url(String wordToFind) {
        return String.format(urlString, wordToFind);
    }
}