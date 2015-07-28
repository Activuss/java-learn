import yotube.ranker.service.RankerService;
import yotube.ranker.service.YoutubeRankerService;
import youtube.ranker.domain.Config;
import youtube.tanker.ui.CliUserInterface;
import youtube.tanker.ui.UserInterface;

public class Main {

    public static void main(String[] args) {
        UserInterface userInterface = new CliUserInterface(args);

        Config config = userInterface.getConfig();

        RankerService rankerService = new YoutubeRankerService(config);

        rankerService.rankVideos();

        userInterface.printResult(rankerService.getTop());
    }
}