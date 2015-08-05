package youtube.ranker.service;

import youtube.ranker.domain.Video;

import java.util.List;

public interface ParserService {
    Video parseVideoInfo(String url);

    List<Video> parseRelatedVideosInfo(Video baseVideo);
}
