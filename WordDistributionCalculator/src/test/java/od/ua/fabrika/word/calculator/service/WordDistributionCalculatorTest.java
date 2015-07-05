package od.ua.fabrika.word.calculator.service;

import od.ua.fabrika.word.calculator.domain.Config;
import od.ua.fabrika.word.calculator.domain.SortType;
import od.ua.fabrika.word.calculator.domain.Storage;
import od.ua.fabrika.word.calculator.domain.WordDistributionStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WordDistributionCalculatorTest {

    private Storage storage;
    private WordDistributionCalculator calculator;

    @Before
    public void setUp(){
        storage = new WordDistributionStorage();
        calculator = new WordDistributionCalculator(new Config(), storage);
        calculator.computeWordStat("c");
        calculator.computeWordStat("b");
        calculator.computeWordStat("a");
        calculator.computeWordStat("c");
    }

    @Test
    public void computeWordStatTest() {
        assertEquals(new Integer(1), new Integer(storage.getOccurrencesNumber("a")));
        assertEquals(new Integer(1), new Integer(storage.getOccurrencesNumber("b")));
        assertEquals(new Integer(2), new Integer(storage.getOccurrencesNumber("c")));
    }

    @Test
    public void sortByAlphabetOrderTest() {
        List<Map.Entry<String, Integer>> sortedList = calculator.sort(SortType.ALPHABET);
        assertEquals("a", sortedList.get(0).getKey());
        assertEquals("b", sortedList.get(1).getKey());
        assertEquals("c", sortedList.get(2).getKey());
    }

    @Test
    public void sortByFrequencyOrderTest() {
        List<Map.Entry<String, Integer>> sortedList = calculator.sort(SortType.FREQUENCY);
        assertEquals(new Integer(1), sortedList.get(0).getValue());
        assertEquals(new Integer(1), sortedList.get(1).getValue());
        assertEquals(new Integer(2), sortedList.get(2).getValue());
    }
}
