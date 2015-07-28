package youtube.ranker.ui;

import org.apache.commons.cli.*;
import youtube.ranker.domain.Config;
import youtube.ranker.domain.Video;
import youtube.ranker.domain.YoutubeRankerException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CliUserInterface implements UserInterface {
    private static final Logger log = Logger.getLogger(CliUserInterface.class.getName());
    private String[] args = null;
    private Options options = new Options();

    public CliUserInterface(String[] args) {
        this.args = args;
        options.addOption("u", "url", true, "Youtube video url (http://www.youtube.com/watch?v=E1tjmHY)");
        options.addOption("r", "related videos", true, "Number of related videos");
        options.addOption("d", "depth", true, "How deep to perform analyze");
    }

    @Override
    public Config getConfig() {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        String url;
        int relatedVideosNumber;
        int depth;

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("u")) {
                url = cmd.getOptionValue("u");
            } else {
                log.log(Level.SEVERE, "Missing url option.");
                help();
                throw new YoutubeRankerException("Illegal command line properties. Missing url option.");
            }

            if (cmd.hasOption("r")) {
                relatedVideosNumber = Integer.parseInt(cmd.getOptionValue("r"));
            } else {
                log.log(Level.SEVERE, "Missing number of related videos option.");
                help();
                throw new YoutubeRankerException("Illegal command line properties. Missing number of related videos option.");
            }

            if (cmd.hasOption("d")) {
                depth = Integer.parseInt(cmd.getOptionValue("d"));
            } else {
                log.log(Level.SEVERE, "Missing depth of analyze option.");
                help();
                throw new YoutubeRankerException("Illegal command line properties. Missing depth of analyze option.");
            }

            log.log(Level.INFO, "Configuration read.");

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties.", e);
            help();
            throw new YoutubeRankerException("Illegal command line properties", e);
        }
        return new Config(url, relatedVideosNumber, depth);
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Main", options);
    }

    @Override
    public void printResult(List<Video> topVideos) {
        System.out.println("Top videos:");
        for (Video video : topVideos) {
            System.out.format("View-count: %d, Url: %s, Name: %s", video.getWatchCounter(), video.getUrl(), video.getName());
            System.out.println();
        }
    }
}
