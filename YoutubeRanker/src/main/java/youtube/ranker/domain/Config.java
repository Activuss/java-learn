package youtube.ranker.domain;

public class Config {
    private String rootUrl;
    private int relatedVideosNumber;
    private int analyzeDepthNumber;

    public Config(String rootUrl, int relatedVideosNumber, int analyzeDepthNumber) {
        this.rootUrl = rootUrl;
        this.relatedVideosNumber = relatedVideosNumber;
        this.analyzeDepthNumber = analyzeDepthNumber;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public int getRelatedVideosNumber() {
        return relatedVideosNumber;
    }

    public int getAnalyzeDepthNumber() {
        return analyzeDepthNumber;
    }
}
