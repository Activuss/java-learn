package youtube.ranker.service;

import youtube.ranker.domain.Video;

import java.util.List;

/**
 * Created by Activuss on 05.08.2015.
 */
public interface ParserService {
    Video parseVideoInfo(String url);

    List<Video> parseRelatedVideosInfo(Video baseVideo);
}
