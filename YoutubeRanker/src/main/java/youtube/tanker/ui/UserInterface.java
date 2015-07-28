package youtube.tanker.ui;

import youtube.ranker.domain.Config;
import youtube.ranker.domain.Video;

import java.util.List;

public interface UserInterface {
    Config getConfig();

    void printResult (List<Video> topVideos);
}
