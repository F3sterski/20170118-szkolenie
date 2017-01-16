package ws.libs.dictionary;

import one.util.streamex.StreamEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BablaDictionaryClient implements DictionaryClient {

    private static final Logger log = Logger.getLogger(BablaDictionaryClient.class.getName());
    private final String urlString;

    public BablaDictionaryClient() {
        this.urlString = "http://pl.bab.la/slownik/angielski-polski/%s";
    }

    public BablaDictionaryClient(String urlString) {
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

            List<String> wholeFile = reader.lines().collect(Collectors.toList());

            return StreamEx.of(wholeFile)
                    .flatMap(englishWordPattern::apply)
                    .zipWith(StreamEx.of(wholeFile)
                            .flatMap(polishWordPattern::apply)
                    )
                    .map(e -> new DictionaryWord(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.warning(String.format("Couldn't process the stream %s. Empty list", url(wordToFind)));
            return Collections.emptyList();
        }
    }

    private String url(String wordToFind) {
        return String.format(urlString, wordToFind.replace(" ", "-"));
    }

    final BiFunction<String, Pattern, Stream<String>> genericMatcher = (s, pattern) -> {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return Stream.of(matcher.group(matcher.groupCount()));
        } else {
            return Stream.empty();
        }
    };

    final Function<String, Stream<String>> polishWordPattern = s ->
            genericMatcher.apply(s, Pattern.compile(".*result-right.*/slownik/polski-angielski/.*\">" +
                    "(.*)" +
                    "</a>.*"));

    final Function<String, Stream<String>> englishWordPattern = s ->
            genericMatcher.apply(s, Pattern.compile(".*result-left.*/slownik/angielski-polski/.*\">" +
                    "<strong>(.*)</strong>" +
                    ".*"));
}