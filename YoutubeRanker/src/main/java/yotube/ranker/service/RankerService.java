package yotube.ranker.service;

import youtube.ranker.domain.Video;

public interface RankerService {
    Video extractFullVideoInfo(String videoUrl);
    String cacheHtmlDocument(String videoUrl);

    void analyzeRelatedVideos(Video rootVideo);
}
