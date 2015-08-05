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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HtmlParserService implements ParserService {
    public static final String HTTP_PROTOCOL_PREFIX = "http";
    public static final String PROTOCOL_SEPARATOR = "://";
    public static final String BASE_URL = "www.youtube.com";
    public static final String VIDEO_PREFIX = "/watch?v";
    public static final int INITIAL_DEPTH_LEVEL = 1;
    private final int relatedVideosNumber;
    private Map<String, Document> documentsCache;

    public HtmlParserService(Config config) {
        this.relatedVideosNumber = config.getRelatedVideosNumber();
        documentsCache = new HashMap<>();
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

    @Override
    public Video parseVideoInfo(String url) {
        String fullUrl = getFullUrl(url);
        cacheHtmlDocument(fullUrl);
        Document videoHtmlDocument = documentsCache.get(url);
        String videoTitle = videoHtmlDocument.title();
        int viewCounter = extractWatchCounter(videoHtmlDocument.getElementsByClass("watch-view-count").text());
        return new Video(url, videoTitle, viewCounter, INITIAL_DEPTH_LEVEL);
    }

    @Override
    public List<Video> parseRelatedVideosInfo(Video baseVideo) {
        String fullVideoUrl = baseVideo.getUrl();
        List<Video> parsedVideos = new ArrayList<>();
        cacheHtmlDocument(fullVideoUrl);
        Document videoHtmlDocument = documentsCache.get(fullVideoUrl);
        Elements relatedVideosPageBlock = videoHtmlDocument.select(".content-wrapper");

        for (int i = 0; (i < relatedVideosPageBlock.size() - 1) && (i < relatedVideosNumber); i++) {
            Element relatedVideoEntry = relatedVideosPageBlock.get(i);
            Elements linkBlock = relatedVideoEntry.select("a");
            String linkTitle = linkBlock.attr("title");
            String relatedVideoUrl = linkBlock.attr("href");
            int relatedVideoViewCount = extractWatchCounter(relatedVideoEntry.select(".view-count").text());
            Video relatedVideo = new Video(getFullUrl(relatedVideoUrl), linkTitle,
                    relatedVideoViewCount, baseVideo.getDeeplevel() + 1);
            baseVideo.addRelatedVideo(relatedVideo);
            parsedVideos.add(relatedVideo);
        }
        documentsCache.remove(fullVideoUrl);
        return parsedVideos;
    }
}
