import yotube.ranker.service.RankerService;
import yotube.ranker.service.YoutubeRankerService;
import youtube.ranker.domain.Video;

public class Main {

    public static void main(String[] args) {
        RankerService rankerService = new YoutubeRankerService(5, 3);
        Video video = rankerService.extractFullVideoInfo("http://www.youtube.com/watch?v=E1tjmHYUHb4");
        System.out.println(video);
        System.out.println("=====");
        rankerService.analyzeRelatedVideos(video);
        System.out.println(video);

    }
}