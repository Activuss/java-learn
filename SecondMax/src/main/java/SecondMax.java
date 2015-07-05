public class SecondMax {
    public static int findSecondMax(int[] inputArray) {
        if (inputArray == null || inputArray.length < 2) {
            throw new IllegalArgumentException("Input array length must be more then 2.");
        }
        int max = inputArray[0];
        int secondMax = max;

        for (int currentElement : inputArray) {
            if (currentElement > max) {
                secondMax = max;
                max = currentElement;
            } else if (currentElement > secondMax) {
                secondMax = currentElement;
            } else if (max == secondMax && currentElement < max) {
                secondMax = currentElement;
            }
        }
        if (max != secondMax) {
            return secondMax;
        } else {
            throw new RuntimeException("Unable to find second max element");
        }
    }
}
