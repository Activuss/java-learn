package od.ua.fabrika.word.calculator.utils;

import od.ua.fabrika.word.calculator.service.IntegerValueComparator;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static od.ua.fabrika.word.calculator.utils.Util.sortMapByValue;


public class UtilTest {

    @Test
    public void sortByValueTest() {
        Map<String, Integer> unsortedMap = new HashMap<>();
        unsortedMap.put("key1", 3);
        unsortedMap.put("key2", 1);
        unsortedMap.put("key3", 2);

        List<Map.Entry<String, Integer>> sortedMap = sortMapByValue(unsortedMap, new IntegerValueComparator());
        assertEquals(new Integer(1), sortedMap.get(0).getValue());
        assertEquals(new Integer(2), sortedMap.get(1).getValue());
        assertEquals(new Integer(3), sortedMap.get(2).getValue());
    }

    @Test(expected = NullPointerException.class)
    public void sortByValueNPETest() {
        sortMapByValue(null, null);
    }
}
