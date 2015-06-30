public class Config {
    private String inputFile;
    private String outputFile;
    private SortType sortType;

    public Config addInputFile (String inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public Config addOutputFile (String outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public Config addSortType(SortType sortType) {
        this.sortType = sortType;
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

    @Override
    public String toString() {
        return "Config{" +
                "inputFile='" + inputFile + '\'' +
                ", outputFile='" + outputFile + '\'' +
                ", addSortType=" + sortType +
                '}';
    }
}
