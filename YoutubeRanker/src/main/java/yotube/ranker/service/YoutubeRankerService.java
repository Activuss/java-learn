package yotube.ranker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import youtube.ranker.domain.Video;
import youtube.ranker.domain.YoutubeRankerException;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class YoutubeRankerService implements RankerService {
    public static final String BASE_URL = "http://www.youtube.com";
    private int relatedVideosNumber;
    private int analysDepthNumber;

    public YoutubeRankerService(int relatedVideosNumber, int analysDepthNumber) {
        this.relatedVideosNumber = relatedVideosNumber;
        this.analysDepthNumber = analysDepthNumber;
    }

    @Override
    public int extractWatchCounter(String countInfo) {
        return Integer.parseInt(countInfo.replaceAll("\\D+", ""));
    }

    @Override
    public Document getHtmlDocument(String videoUrl) {
        Document htmlDocument;

        if (!videoUrl.contains(BASE_URL))
            videoUrl = BASE_URL + videoUrl;

        try {
            htmlDocument = Jsoup.connect(videoUrl).userAgent("Mozilla").get();
        } catch (SocketTimeoutException e) {
            throw new YoutubeRankerException("Unable to reach ip: " + videoUrl, e);
        } catch (IOException e) {
            throw new YoutubeRankerException("Unable to get html document: " + videoUrl, e);
        }
        return htmlDocument;
    }

    @Override
    public Video extractVideoInfo(String videoUrl) {
        Document videoHtmlDocument = getHtmlDocument(videoUrl);
        String videoTitle = videoHtmlDocument.title();
        int watchCounter = extractWatchCounter(videoHtmlDocument.getElementsByClass("watch-view-count").text());
        Video parsedVideo = new Video(videoUrl, videoTitle, watchCounter);

        Elements relatedVideos = videoHtmlDocument.select(".content-wrapper");

        for (int i = 0; i < relatedVideos.size() - 1 && i < relatedVideosNumber; i++) {
            Element relatedVideoDivision = relatedVideos.get(i);
            Elements tagA = relatedVideoDivision.select("a");
            String relatedVideoTitle = tagA.attr("title");
            String relatedVideoUrl = tagA.attr("href");
            int relatedVideoWatchCount = extractWatchCounter(relatedVideoDivision.select(".view-count").text());
            Video relatedVideo = new Video(relatedVideoUrl, relatedVideoTitle, relatedVideoWatchCount);
            parsedVideo.addRelatedVideo(relatedVideo);
        }
        return parsedVideo;
    }
}
