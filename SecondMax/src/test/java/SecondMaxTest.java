import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SecondMaxTest {

    @Test(expected = IllegalArgumentException.class)
    public void tooSmallArrayTest() {
        SecondMax.findSecondMax(new int[]{1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooEmptyArrayTest() {
        SecondMax.findSecondMax(null);
    }

    @Test
    public void ascendingTest() {
        assertEquals(3, SecondMax.findSecondMax(new int[]{1, 2, 3, 4}));
    }

    @Test
    public void ascendingSmallArrayTest() {
        assertEquals(1, SecondMax.findSecondMax(new int[]{1, 2}));
    }


    @Test
    public void decendingTest() {
        assertEquals(3, SecondMax.findSecondMax(new int[]{4, 3, 2, 1}));
    }

    @Test(expected = RuntimeException.class)
    public void equalTest() {
        SecondMax.findSecondMax(new int[]{4, 4, 4, 4});
    }

    @Test
    public void equalStartTest() {
        assertEquals(3, SecondMax.findSecondMax(new int[]{4, 4, 2, 3}));
    }

    @Test
    public void equalEndTest() {
        assertEquals(3, SecondMax.findSecondMax(new int[]{4, 2, 3, 3}));
    }

    @Test
    public void equalStartEndTest() {
        assertEquals(3, SecondMax.findSecondMax(new int[]{4, 4, 2, 3, 3}));
    }

    @Test
    public void negativeTest() {
        assertEquals(-2, SecondMax.findSecondMax(new int[]{-1, -2, -3, -4}));
    }


}
