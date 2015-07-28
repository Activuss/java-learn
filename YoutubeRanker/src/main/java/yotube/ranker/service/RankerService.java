package yotube.ranker.service;

import youtube.ranker.domain.Video;

import java.util.List;

public interface RankerService {
    void rankVideos();

    List<Video> getTop();
}
