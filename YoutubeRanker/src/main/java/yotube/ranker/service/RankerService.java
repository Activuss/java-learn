package yotube.ranker.service;

import org.jsoup.nodes.Document;
import youtube.ranker.domain.Video;

public interface RankerService {
    Video extractFullVideoInfo(String videoUrl);
    String cacheHtmlDocument(String videoUrl);
}
