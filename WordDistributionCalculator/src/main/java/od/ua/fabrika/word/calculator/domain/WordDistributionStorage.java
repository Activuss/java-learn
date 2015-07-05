package od.ua.fabrika.word.calculator.domain;

import java.util.HashMap;
import java.util.Map;

public class WordDistributionStorage implements Storage {
    Map<String, Integer> storage = new HashMap<>();

    public boolean contains(String word) {
        return storage.containsKey(word);
    }

    public void putOrReplace(String word, Integer occurrencesNumber) {
        storage.put(word, occurrencesNumber);
    }

    @Override
    public int getOccurrencesNumber(String word) {
        return storage.get(word);
    }

    @Override
    public Map<String, Integer> getData() {
        return storage;
    }
}
