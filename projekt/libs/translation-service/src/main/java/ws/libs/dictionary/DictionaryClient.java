package ws.libs.dictionary;

import java.util.List;
import java.util.Optional;

public interface DictionaryClient {

	Optional<DictionaryWord> firstTranslationFor(String word);
	List<DictionaryWord> allTranslationsFor(String word);

}
