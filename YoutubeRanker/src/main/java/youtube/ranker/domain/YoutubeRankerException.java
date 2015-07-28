package youtube.ranker.domain;

public class YoutubeRankerException extends RuntimeException {
    public YoutubeRankerException(String message) {
        super(message);
    }

    public YoutubeRankerException(String message, Throwable cause) {
        super(message, cause);
    }
}
