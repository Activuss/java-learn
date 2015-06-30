import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WordDistributionCalculator {

    private static final Logger log = Logger.getLogger(WordDistributionCalculator.class.getName());
    private Storage wordDistributionStorage;
    private Config config;

    public WordDistributionCalculator(Config config, Storage wordDistributionStorage) {
        this.config = config;
        this.wordDistributionStorage = wordDistributionStorage;
    }

    private void computeWordStat(String word) {
        int occurrencesNumber;
        if (!wordDistributionStorage.contains(word)) {
            occurrencesNumber = 1;
        } else {
            occurrencesNumber = wordDistributionStorage.getOccurrencesNumber(word) + 1;
        }

        wordDistributionStorage.putOrReplace(word, occurrencesNumber);
    }

    private List<Map.Entry<String, Integer>> sort(SortType sortType) {
        log.log(Level.INFO, "Sorting by " + sortType.toString().toLowerCase() + " order." );
        switch (sortType) {
            case NATURAL:
                return Collections.unmodifiableList(new LinkedList<>(wordDistributionStorage.getData().entrySet()));

            case ALPHABET:
                Map<String, Integer> naturalOrderedMap = new TreeMap<>(wordDistributionStorage.getData());
                return Collections.unmodifiableList(new LinkedList<>(naturalOrderedMap.entrySet()));

            case FREQUENCY:
                return Util.sortMapByValue(wordDistributionStorage.getData(), new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> o1,
                                       Map.Entry<String, Integer> o2) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    }
                });

            default: return Collections.emptyList();
        }
    }

    public void calculateStats() {
        try {
            List<String> fileLines = Util.readFile(config.getInputFile());
            for (String line : fileLines) {
                String [] words = Util.parseWords(line);
                for (String word : words) {
                    if (word.length() > 0) {
                        computeWordStat(word);
                    }
                }
            }
            Util.writeFile(config.getOutputFile(), sort(config.getSortType()));
        } catch (IOException e) {
            log.log(Level.SEVERE, "Some IO error during execution of program.", e);
        }
    }

    public static void main(String[] args) {
        Cli cli = new Cli(args);
        Config config = cli.parseConfig();
        WordDistributionStorage storage = new WordDistributionStorage();

        new WordDistributionCalculator(config, storage).calculateStats();
    }
}
