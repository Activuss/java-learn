package youtube.ranker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import youtube.ranker.domain.Config;
import youtube.ranker.domain.Video;
import youtube.ranker.domain.YoutubeRankerException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YoutubeRankerService implements RankerService {
    private static final Logger log = Logger.getLogger(YoutubeRankerService.class.getName());
    public static final String HTTP_PROTOCOL_PREFIX = "http";
    public static final String PROTOCOL_SEPARATOR = "://";
    public static final String BASE_URL = "www.youtube.com";
    public static final String VIDEO_PREFIX = "/watch?v";
    public static final int NUMBER_OF_TOP_ENTRIES = 10;
    public static final int INITIAL_DEPTH_LEVEL = 1;
    private Map<String, Document> documentsCache;
    private List<Video> parsedVideos;
    private int relatedVideosNumber;
    private int analyzeDepthNumber;
    private String rootUrl;

    public YoutubeRankerService(Config configuration) {
        this.relatedVideosNumber = configuration.getRelatedVideosNumber();
        this.analyzeDepthNumber = configuration.getAnalyzeDepthNumber();
        this.rootUrl = configuration.getRootUrl();
        documentsCache = new HashMap<>();
        parsedVideos = new ArrayList<>();
    }

    private int extractWatchCounter(String countInfo) {
        return Integer.parseInt(countInfo.replaceAll("\\D+", ""));
    }

    private String getFullUrl(String url) {
        if (url.startsWith(HTTP_PROTOCOL_PREFIX) && url.contains(BASE_URL) && url.contains(VIDEO_PREFIX)) {
            return url;
        } else if (url.startsWith(BASE_URL) && url.contains(VIDEO_PREFIX)) {
            return HTTP_PROTOCOL_PREFIX + PROTOCOL_SEPARATOR + url;
        } else if (url.startsWith(VIDEO_PREFIX)) {
            return HTTP_PROTOCOL_PREFIX + PROTOCOL_SEPARATOR + BASE_URL + url;
        } else {
            throw new YoutubeRankerException("Incorrect youtube link.");
        }
    }

    private void cacheHtmlDocument(String fullUrl) {
        if (!documentsCache.containsKey(fullUrl))
            try {
                Document htmlDocument = Jsoup.connect(fullUrl).userAgent("Mozilla").get();
                documentsCache.put(fullUrl, htmlDocument);
            } catch (SocketTimeoutException e) {
                throw new YoutubeRankerException("Unable to reach ip: " + fullUrl, e);
            } catch (IOException e) {
                throw new YoutubeRankerException("Unable to get html document: " + fullUrl, e);
            }
    }

    private Video parseVideoInfo(String url) {
        Document videoHtmlDocument = documentsCache.get(url);
        String videoTitle = videoHtmlDocument.title();
        int watchCounter = extractWatchCounter(videoHtmlDocument.getElementsByClass("watch-view-count").text());
        return new Video(url, videoTitle, watchCounter, INITIAL_DEPTH_LEVEL);
    }

    private void parseRelatedVideosInfo(Video video) {
        String fullVideoUrl = video.getUrl();

        if (!documentsCache.containsKey(fullVideoUrl)) {
            cacheHtmlDocument(fullVideoUrl);
        }
        Document videoHtmlDocument = documentsCache.get(fullVideoUrl);
        Elements relatedVideos = videoHtmlDocument.select(".content-wrapper");

        for (int i = 0; (i < relatedVideos.size() - 1) && (i < relatedVideosNumber); i++) {
            Element relatedVideoDivision = relatedVideos.get(i);
            Elements tagA = relatedVideoDivision.select("a");
            String relatedVideoTitle = tagA.attr("title");
            String relatedVideoUrl = tagA.attr("href");
            String viewCountString = relatedVideoDivision.select(".view-count").text();
            if (!viewCountString.isEmpty()) {
                int relatedVideoWatchCount = extractWatchCounter(viewCountString);
                Video relatedVideo = new Video(getFullUrl(relatedVideoUrl), relatedVideoTitle, relatedVideoWatchCount, video.getDeeplevel() + 1);
                video.addRelatedVideo(relatedVideo);
                parsedVideos.add(relatedVideo);
            }
        }

        documentsCache.remove(fullVideoUrl);
    }

    private Video analyzeRootVideo() {
        log.log(Level.INFO, "Start analyze.");
        String fullUrl = getFullUrl(rootUrl);
        cacheHtmlDocument(fullUrl);
        Video rootVideo = parseVideoInfo(fullUrl);
        parsedVideos.add(rootVideo);
        parseRelatedVideosInfo(rootVideo);

        return rootVideo;
    }

    private void analyzeRelatedVideos(Video rootVideo) {
        for (Video video : rootVideo.getRelatedVideos()) {
            if (video.getDeeplevel() > analyzeDepthNumber)
                continue;
            parseRelatedVideosInfo(video);
            analyzeRelatedVideos(video);
        }
    }

    @Override
    public void rankVideos() {
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
