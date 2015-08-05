package youtube.ranker.domain;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private final String url;
    private final String name;
    private final long viewCounter;
    private final List<Video> relatedVideos;
    private final int deeplevel;

    public Video(String url, String name, long viewCounter, int deeplevel) {
        this.url = url;
        this.name = name;
        this.viewCounter = viewCounter;
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

    public long getViewCounter() {
        return viewCounter;
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
        string.append('\'' + '\'' + ", viewCounter=");
        string.append(viewCounter);
        string.append("}");
        string.append(separator);
        string.append(" related: ");
        string.append(relatedVideos.size());
        string.append(" level ");
        string.append(deeplevel);
        string.append(" \n");

        for (Video relatedVideo : relatedVideos) {
            string.append(separator);
            string.append(relatedVideo);
        }

        return string.toString();
    }
}
