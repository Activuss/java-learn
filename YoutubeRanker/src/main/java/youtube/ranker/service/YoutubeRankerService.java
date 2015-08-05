package youtube.ranker.service;

import youtube.ranker.domain.Config;
import youtube.ranker.domain.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YoutubeRankerService implements RankerService {
    private static final Logger log = Logger.getLogger(YoutubeRankerService.class.getName());
    private static final int NUMBER_OF_TOP_ENTRIES = 10;
    private final ParserService parser;
    private List<Video> parsedVideos;
    private final String rootUrl;
    private final int analyzeDepthNumber;


    public YoutubeRankerService(Config configuration, ParserService parser) {
        this.analyzeDepthNumber = configuration.getAnalyzeDepthNumber();
        this.rootUrl = configuration.getRootUrl();
        this.parser = parser;
        parsedVideos = new ArrayList<>();
    }

    @Override
    public Video analyzeRootVideo() {
        log.log(Level.INFO, "Start analyze.");
        Video rootVideo = parser.parseVideoInfo(rootUrl);
        parsedVideos.add(rootVideo);
        parser.parseRelatedVideosInfo(rootVideo);

        return rootVideo;
    }

    @Override
    public void analyzeRelatedVideos(Video baseVideo) {
        for (Video relatedVideo : baseVideo.getRelatedVideos()) {
            if (relatedVideo.getDeeplevel() > analyzeDepthNumber)
                continue;
            List<Video> relatedVideos = parser.parseRelatedVideosInfo(relatedVideo);
            parsedVideos.addAll(relatedVideos);
            analyzeRelatedVideos(relatedVideo);
        }
    }

    @Override
    public void buildVideoHierarchy() {
        Video video = analyzeRootVideo();
        analyzeRelatedVideos(video);
    }

    @Override
    public List<Video> getTop() {
        log.log(Level.INFO, "Sorting results.");
        Set<Video> sortedVideos = new TreeSet<>(parsedVideos);
        List<Video> topVideos = new ArrayList<>();
        int addedVideosCount = 0;

        for (Video sortedVideo : sortedVideos) {
            if (addedVideosCount <= NUMBER_OF_TOP_ENTRIES) {
                topVideos.add(sortedVideo);
                addedVideosCount++;
            } else {
                break;
            }
        }
        return topVideos;
    }
}
