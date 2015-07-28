package youtube.ranker.domain;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String url;
    private String name;
    private long watchCounter;
    private List<Video> relatedVideos;
    private int deeplevel;

    public Video(String url, String name, long watchCounter, int deeplevel) {
        this.url = url;
        this.name = name;
        this.watchCounter = watchCounter;
        this.deeplevel = deeplevel;
        relatedVideos = new ArrayList<>();
    }

    public void addRelatedVideo(Video relatedVideo) {
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

    public int getDeeplevel() {
        return deeplevel;
    }

    @Override
    public String toString() {
        StringBuilder separator = new StringBuilder();
        int j = deeplevel;

        while (j >= 0) {
            separator.append(" - ");
            j--;
        }
        StringBuilder string = new StringBuilder();
        string.append("Video{url='");
        string.append(url);
        string.append('\'' + '\'' + ", watchCounter=");
        string.append(watchCounter);
        string.append("}");
        string.append(separator);
        string.append(" related: ");
        string.append(relatedVideos.size());
        string.append(" \n");

        for (Video relatedVideo : relatedVideos) {
            string.append(separator);
            string.append(relatedVideo);
        }

        return string.toString();
    }
}
