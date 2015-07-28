package yotube.ranker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import youtube.ranker.domain.Video;
import youtube.ranker.domain.YoutubeRankerException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class YoutubeRankerService implements RankerService {
    public static final String BASE_URL = "http://www.youtube.com";
    private int relatedVideosNumber;
    private int analysDepthNumber;
    private Map<String, Document> documentsCache;

    public YoutubeRankerService(int relatedVideosNumber, int analysDepthNumber) {
        this.relatedVideosNumber = relatedVideosNumber;
        this.analysDepthNumber = analysDepthNumber;
        documentsCache = new HashMap<>();
    }

    public int extractWatchCounter(String countInfo) {
        return Integer.parseInt(countInfo.replaceAll("\\D+", ""));
    }

    @Override
    public String cacheHtmlDocument(String videoUrl) {

        String cacheKey = videoUrl;

        if (!videoUrl.contains(BASE_URL))
            cacheKey = BASE_URL + videoUrl;

        if (!documentsCache.containsKey(cacheKey))
            try {
                Document htmlDocument = Jsoup.connect(cacheKey).userAgent("Mozilla").get();
                documentsCache.put(cacheKey, htmlDocument);
            } catch (SocketTimeoutException e) {
                throw new YoutubeRankerException("Unable to reach ip: " + cacheKey, e);
            } catch (IOException e) {
                throw new YoutubeRankerException("Unable to get html document: " + cacheKey, e);
            }
        return cacheKey;
    }

    private Video parseVideoInfo(String url) {
        Document videoHtmlDocument = documentsCache.get(url);
        String videoTitle = videoHtmlDocument.title();
        int watchCounter = extractWatchCounter(videoHtmlDocument.getElementsByClass("watch-view-count").text());
        return new Video(url, videoTitle, watchCounter);
    }

    private void fillRelatedVideosInfo(Video video) {
        Document videoHtmlDocument = documentsCache.get(video.getUrl());
        Elements relatedVideos = videoHtmlDocument.select(".content-wrapper");

        for (int i = 0; i < relatedVideos.size() - 1 && i < relatedVideosNumber; i++) {
            Element relatedVideoDivision = relatedVideos.get(i);
            Elements tagA = relatedVideoDivision.select("a");
            String relatedVideoTitle = tagA.attr("title");
            String relatedVideoUrl = tagA.attr("href");
            int relatedVideoWatchCount = extractWatchCounter(relatedVideoDivision.select(".view-count").text());
            Video relatedVideo = new Video(relatedVideoUrl, relatedVideoTitle, relatedVideoWatchCount);
            video.addRelatedVideo(relatedVideo);
        }
    }

    @Override
    public Video extractFullVideoInfo(String videoUrl) {
        String cacheKey = cacheHtmlDocument(videoUrl);
        Video parsedVideo = parseVideoInfo(cacheKey);
        fillRelatedVideosInfo(parsedVideo);

        return parsedVideo;
    }
}
