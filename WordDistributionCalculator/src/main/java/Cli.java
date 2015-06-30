import org.apache.commons.cli.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();

    public Cli(String[] args) {

        this.args = args;

        options.addOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");
        options.addOption("a", "alphabet", false, "Sort by alphabet");
        options.addOption("f", "frequency", false, "Sort by frequency");

    }

    public Config parseConfig() {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        Config config = new Config();

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("i")) {
                config.addInputFile(cmd.getOptionValue("i"));
            } else {
                log.log(Level.SEVERE, "Missing i option");
                help();
            }

            if (cmd.hasOption("o")) {
                config.addOutputFile(cmd.getOptionValue("o"));
            } else {
                log.log(Level.SEVERE, "Missing o option");
                help();
            }

            if (cmd.hasOption("a")) {
                config.addSortType(SortType.ALPHABET);
            } else if (cmd.hasOption("f")) {
                config.addSortType(SortType.FREQUENCY);
            } else {
                config.addSortType(SortType.NATURAL);
            }
            log.log(Level.INFO, "Finished building configuration.");

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties.", e);
            help();
        }
        return config;
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Main", options);
        System.exit(0);
    }
}