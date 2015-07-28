package youtube.ranker.domain;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String url;
    private String name;
    private long watchCounter;
    private List<Video> relatedVideos;

    public Video(String url, String name, long watchCounter) {
        this.url = url;
        this.name = name;
        this.watchCounter = watchCounter;
        relatedVideos = new ArrayList<>();
    }

    public void addRelatedVideo (Video relatedVideo) {
        relatedVideos.add(relatedVideo);
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public long getWatchCounter() {
        return watchCounter;
    }

    public List<Video> getRelatedVideos() {
        return relatedVideos;
    }

    @Override
    public String toString() {
        return "Video{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", watchCounter=" + watchCounter +
                ", relatedVideos=" + relatedVideos +
                '}';
    }
}
