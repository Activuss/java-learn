import yotube.ranker.service.RankerService;
import yotube.ranker.service.YoutubeRankerService;

public class Main {

    public static void main(String[] args) {
        RankerService rankerService = new YoutubeRankerService(1, 5);
        System.out.println(rankerService.extractFullVideoInfo("http://www.youtube.com/watch?v=E1tjmHYUHb4"));
    }

}