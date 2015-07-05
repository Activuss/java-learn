package od.ua.fabrika.word.calculator.service;

import od.ua.fabrika.word.calculator.domain.*;
import od.ua.fabrika.word.calculator.utils.CommandLineConfigurator;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static od.ua.fabrika.word.calculator.utils.Util.*;


public class WordDistributionCalculator {

    private static final Logger log = Logger.getLogger(WordDistributionCalculator.class.getName());
    private static final String DICTIONARY_NAME = "wordlist.txt";
    private Storage wordDistributionStorage;
    private Config config;
    private List<String> dictionary;

    public WordDistributionCalculator(Config config, Storage wordDistributionStorage) {
        this.config = config;
        this.wordDistributionStorage = wordDistributionStorage;
        if (config.isRealDictionary()) {
            dictionary = new ArrayList<>(Arrays.asList(readResource(DICTIONARY_NAME)));
        }
    }

    private void computeWordStat(String word) {
        if (!wordDistributionStorage.contains(word)) {
            wordDistributionStorage.putOrReplace(word, 1);
            return;
        }
        wordDistributionStorage.putOrReplace(word, wordDistributionStorage.getOccurrencesNumber(word) + 1);
    }

    private List<Map.Entry<String, Integer>> sort(SortType sortType) {
        log.log(Level.INFO, "Sorting by " + sortType.toString().toLowerCase() + " order.");
        switch (sortType) {
            case NATURAL:
                return Collections.unmodifiableList(new LinkedList<>(wordDistributionStorage.getData().entrySet()));
            case ALPHABET:
                Map<String, Integer> alphabeticSortedDictionary = new TreeMap<>(wordDistributionStorage.getData());
                return Collections.unmodifiableList(new LinkedList<>(alphabeticSortedDictionary.entrySet()));
            case FREQUENCY:
                return sortMapByValue(wordDistributionStorage.getData(), new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> o1,
                                       Map.Entry<String, Integer> o2) {
                        return (o1.getValue()).compareTo(o2.getValue());
                    }
                });
            default:
                return Collections.emptyList();
        }
    }

    public void calculateStats() {
        log.log(Level.INFO, "Calculating stats.");
        try {
            List<String> fileLines = readFile(config.getInputFile());
            boolean useRealDictionary = config.isRealDictionary();

            for (String line : fileLines) {
                String[] words = parseWords(line);
                for (String word : words) {
                    String wordInLowerCase = word.toLowerCase();
                    if ((word.length() > 0)) {
                        if (!useRealDictionary || wordDistributionStorage.contains(wordInLowerCase)
                                || dictionary.contains(wordInLowerCase)) {
                            computeWordStat(wordInLowerCase);
                        }
                    }
                }
            }
            writeFile(config.getOutputFile(), sort(config.getSortType()));
        } catch (ApplicationException e) {
            log.log(Level.SEVERE, "Some IO error during execution of program.", e);
        }
    }

    public static void main(String[] args) {
        CommandLineConfigurator cli = new CommandLineConfigurator(args);
        Config config = cli.parseConfig();
        Storage storage = new WordDistributionStorage();
        new WordDistributionCalculator(config, storage).calculateStats();
    }
}

