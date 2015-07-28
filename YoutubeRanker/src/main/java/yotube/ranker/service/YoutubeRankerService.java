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
    private int analyzeDepthNumber;
    private Map<String, Document> documentsCache;

    public YoutubeRankerService(int relatedVideosNumber, int analyzeDepthNumber) {
        this.relatedVideosNumber = relatedVideosNumber;
        this.analyzeDepthNumber = analyzeDepthNumber;
        documentsCache = new HashMap<>();
    }

    public int extractWatchCounter(String countInfo) {
        return Integer.parseInt(countInfo.replaceAll("\\D+", ""));
    }

    private String getFullUrl(String url) {
        String fullUrl = url;
        if (!url.contains(BASE_URL))
            fullUrl = BASE_URL + url;
        return fullUrl;
    }

    @Override
    public String cacheHtmlDocument(String videoUrl) {
        //TODO get and use shortUrl
        String fullUrl = getFullUrl(videoUrl);

        if (!documentsCache.containsKey(fullUrl))
            try {
                Document htmlDocument = Jsoup.connect(fullUrl).userAgent("Mozilla").get();
                documentsCache.put(fullUrl, htmlDocument);
            } catch (SocketTimeoutException e) {
                throw new YoutubeRankerException("Unable to reach ip: " + fullUrl, e);
            } catch (IOException e) {
                throw new YoutubeRankerException("Unable to get html document: " + fullUrl, e);
            }
        return fullUrl;
    }

    private Video parseVideoInfo(String url) {
        Document videoHtmlDocument = documentsCache.get(url);
        String videoTitle = videoHtmlDocument.title();
        int watchCounter = extractWatchCounter(videoHtmlDocument.getElementsByClass("watch-view-count").text());
        return new Video(url, videoTitle, watchCounter, 1);
    }

    private void fillRelatedVideosInfo(Video video) {
        String fullVideoUrl = getFullUrl(video.getUrl());

        if (!documentsCache.containsKey(fullVideoUrl)) {
            cacheHtmlDocument(fullVideoUrl);
        }
        Document videoHtmlDocument = documentsCache.get(fullVideoUrl);
        Elements relatedVideos = videoHtmlDocument.select(".content-wrapper");

        for (int i = 0; i < relatedVideos.size() - 1 && i < relatedVideosNumber; i++) {
            Element relatedVideoDivision = relatedVideos.get(i);
            Elements tagA = relatedVideoDivision.select("a");
            String relatedVideoTitle = tagA.attr("title");
            String relatedVideoUrl = tagA.attr("href");
            int relatedVideoWatchCount = extractWatchCounter(relatedVideoDivision.select(".view-count").text());
            Video relatedVideo = new Video(getFullUrl(relatedVideoUrl), relatedVideoTitle, relatedVideoWatchCount, video.getDeeplevel() + 1);
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

    @Override
    public void analyzeRelatedVideos(Video rootVideo) {
        while (analyzeDepthNumber >= 1) {
            analyzeDepthNumber--;
            for (Video video : rootVideo.getRelatedVideos()) {//
                fillRelatedVideosInfo(video);
                for (Video relatedVideo : video.getRelatedVideos()) {
                    analyzeRelatedVideos(relatedVideo);
                }
            }
        }
    }
}
