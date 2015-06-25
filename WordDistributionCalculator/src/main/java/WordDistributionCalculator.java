import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordDistributionCalculator {

    private Map<String, Integer> wordDistributionStorage = new HashMap<>();

    private List<String> readFile (String filename) throws IOException {
        return FileUtils.readLines(new File(filename), "UTF-8");
    }

    private String[] parseWords (String fileLine) {
        return fileLine.split("[\\W]");
    }

    private void analyseAndSaveWordStat (String word) {
        if (!wordDistributionStorage.containsKey(word)) {
            wordDistributionStorage.put(word, 1);
        } else {
            int currentCount = wordDistributionStorage.remove(word);
            wordDistributionStorage.put(word, ++currentCount);

        }
    }

    public static void main(String[] args) {

        WordDistributionCalculator calculator = new WordDistributionCalculator();

        try {
            List<String> fileLines = calculator.readFile("c:\\Users\\Activuss\\Desktop\\test.txt");
            for (String line : fileLines) {
                String [] words = calculator.parseWords(line);
                for (int i = 0; i < words.length; i++) {
                    calculator.analyseAndSaveWordStat(words[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(calculator.wordDistributionStorage);

    }
}
