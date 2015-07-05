package od.ua.fabrika.word.calculator.domain;

public class Config {
    private String inputFile;
    private String outputFile;
    private boolean isRealDictionary;
    private SortType sortType;

    public Config addInputFile(String inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public Config addOutputFile(String outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public Config addSortType(SortType sortType) {
        this.sortType = sortType;
        return this;
    }

    public Config useRealDictionary(boolean useRealDictionary) {
        this.isRealDictionary = useRealDictionary;
        return this;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public SortType getSortType() {
        return sortType;
    }

    public boolean isRealDictionary() {
        return isRealDictionary;
    }
}
