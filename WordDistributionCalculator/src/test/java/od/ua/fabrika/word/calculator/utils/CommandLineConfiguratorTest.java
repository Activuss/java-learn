package od.ua.fabrika.word.calculator.utils;

import od.ua.fabrika.word.calculator.domain.ApplicationException;
import od.ua.fabrika.word.calculator.domain.Config;
import od.ua.fabrika.word.calculator.domain.SortType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandLineConfiguratorTest {
    private String INPUT_FILE_NAME = "inputFile";
    private String OUTPUT_FILE_NAME = "outputFile";

    @Test()
    public void parseInputOutputFileConfigurationTest() {
        String[] startParameters = {"-i", INPUT_FILE_NAME, "-o", OUTPUT_FILE_NAME};
        CommandLineConfigurator configurator = new CommandLineConfigurator(startParameters);
        Config config = configurator.parseConfig();
        assertEquals(INPUT_FILE_NAME, config.getInputFile());
        assertEquals(OUTPUT_FILE_NAME, config.getOutputFile());
        assertEquals(false, config.isRealDictionary());
        assertEquals(SortType.NATURAL, config.getSortType());
    }

    @Test(expected = ApplicationException.class)
    public void parseInputFileExceptionConfigurationTest() {
        String[] startParameters = {"-o", OUTPUT_FILE_NAME};
        CommandLineConfigurator configurator = new CommandLineConfigurator(startParameters);
        configurator.parseConfig();
    }

    @Test(expected = ApplicationException.class)
    public void parseOotputFileExceptionConfigurationTest() {
        String[] startParameters = {"-i", INPUT_FILE_NAME};
        CommandLineConfigurator configurator = new CommandLineConfigurator(startParameters);
        configurator.parseConfig();
    }

    @Test()
    public void parseDictionaryConfigurationTest() {
        String[] startParameters = {"-i", INPUT_FILE_NAME, "-o", OUTPUT_FILE_NAME, "-d"};
        CommandLineConfigurator configurator = new CommandLineConfigurator(startParameters);
        Config config = configurator.parseConfig();
        assertEquals(true, config.isRealDictionary());
    }

    @Test()
    public void parseAlphabetSortConfigurationTest() {
        String[] startParameters = {"-i", INPUT_FILE_NAME, "-o", OUTPUT_FILE_NAME, "-a"};
        CommandLineConfigurator configurator = new CommandLineConfigurator(startParameters);
        Config config = configurator.parseConfig();
        assertEquals(SortType.ALPHABET, config.getSortType());
    }

    @Test()
    public void parseFrequencySortConfigurationTest() {
        String[] startParameters = {"-i", INPUT_FILE_NAME, "-o", OUTPUT_FILE_NAME, "-f"};
        CommandLineConfigurator configurator = new CommandLineConfigurator(startParameters);
        Config config = configurator.parseConfig();
        assertEquals(SortType.FREQUENCY, config.getSortType());
    }

}
