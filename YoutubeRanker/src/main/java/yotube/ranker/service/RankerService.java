package yotube.ranker.service;

import org.jsoup.nodes.Document;
import youtube.ranker.domain.Video;

public interface RankerService {
    Video extractVideoInfo (String videoUrl);
    int extractWatchCounter (String countInfo);
    Document getHtmlDocument(String videoUrl);
}
