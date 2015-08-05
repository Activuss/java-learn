package youtube.ranker.service;

import youtube.ranker.domain.Video;

import java.util.List;

public interface RankerService {
    Video analyzeRootVideo();

    void analyzeRelatedVideos(Video baseVideo);

    void buildVideoHierarchy();

    List<Video> getTop();
}
