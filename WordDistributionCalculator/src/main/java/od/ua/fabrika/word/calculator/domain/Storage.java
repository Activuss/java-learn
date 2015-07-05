package od.ua.fabrika.word.calculator.domain;

import java.util.Map;

public interface Storage {
    boolean contains(String word);

    void putOrReplace(String word, Integer occurrencesNumber);

    int getOccurrencesNumber(String word);

    Map<String, Integer> getData();
}
