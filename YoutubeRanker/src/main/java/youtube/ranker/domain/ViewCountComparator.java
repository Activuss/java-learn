package youtube.ranker.domain;

import java.util.Comparator;

public class ViewCountComparator implements Comparator<Video> {
    @Override
    public int compare(Video v1, Video v2) {
        return Long.compare(v2.getViewCounter(), v1.getViewCounter());
    }
}
