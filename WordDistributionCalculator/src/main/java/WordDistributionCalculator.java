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
        if (!wordDistributionStorage.contains(word)) {
            wordDistributionStorage.putOrReplace(word, 1);
            return;
        }

        wordDistributionStorage.putOrReplace(word, wordDistributionStorage.getOccurrencesNumber(word) + 1);
    }

    private List<Map.Entry<String, Integer>> sort(SortType sortType) {
        log.log(Level.INFO, "Sorting by " + sortType.toString().toLowerCase() + " order." );
        switch (sortType) {
            case NATURAL:
                return Collections.unmodifiableList(new LinkedList<>(wordDistributionStorage.getData().entrySet()));
            case ALPHABET:
                Map<String, Integer> alphabeticSortedDictionary = new TreeMap<>(wordDistributionStorage.getData());
                return Collections.unmodifiableList(new LinkedList<>(alphabeticSortedDictionary.entrySet()));
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
                        computeWordStat(word.toLowerCase());
                    }
                }
            }
            Util.writeFile(config.getOutputFile(), sort(config.getSortType()));
        } catch (ApplicaionException e) {
            log.log(Level.SEVERE, "Some IO error during execution of program.", e);
        }
    }

    public static void main(String[] args) {
        Cli cli = new Cli(args);
        Config config = cli.parseConfig();
        Storage storage = new WordDistributionStorage();
        new WordDistributionCalculator(config, storage).calculateStats();
    }
}

