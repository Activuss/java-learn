import youtube.ranker.service.RankerService;
import youtube.ranker.service.YoutubeRankerService;
import youtube.ranker.domain.Config;
import youtube.ranker.ui.CliUserInterface;
import youtube.ranker.ui.UserInterface;

public class Main {

    public static void main(String[] args) {
        UserInterface userInterface = new CliUserInterface(args);

        Config config = userInterface.getConfig();

        RankerService rankerService = new YoutubeRankerService(config);

        rankerService.rankVideos();

        userInterface.printResult(rankerService.getTop());
    }
}