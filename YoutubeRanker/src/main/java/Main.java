import youtube.ranker.domain.RankCriteria;
import youtube.ranker.service.HtmlParserService;
import youtube.ranker.service.ParserService;
import youtube.ranker.service.RankerService;
import youtube.ranker.service.YoutubeRankerService;
import youtube.ranker.domain.Config;
import youtube.ranker.ui.CliUserInterface;
import youtube.ranker.ui.UserInterface;

public class Main {

    public static void main(String[] args) {
        UserInterface userInterface = new CliUserInterface(args);

        Config config = userInterface.getConfig();

        ParserService parserService = new HtmlParserService(config);

        RankerService rankerService = new YoutubeRankerService(config, parserService);

        rankerService.buildVideoHierarchy();

        userInterface.printResult(rankerService.getTop(RankCriteria.VIEW_COUNT));
    }
}