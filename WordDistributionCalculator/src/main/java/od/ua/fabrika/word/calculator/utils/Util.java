package od.ua.fabrika.word.calculator.utils;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import od.ua.fabrika.word.calculator.domain.ApplicationException;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static final String ENCODING_UTF_8 = "UTF-8";
    private static final Logger log = Logger.getLogger(Util.class.getName());

    public static List<String> readFile(String filename) {
        log.log(Level.INFO, "Reading file: " + filename);
        try {
            return FileUtils.readLines(new File(filename), ENCODING_UTF_8);
        } catch (IOException e) {
            throw new ApplicationException("Error during reading input file. Check correctness of name and permissions.", e);
        }
    }

    public static String[] readResource(String resourceName) {
        log.log(Level.INFO, "Reading a dictionary. ");
        try {
            URL url = Resources.getResource(resourceName);
            String dictionary = Resources.toString(url, Charsets.UTF_8);
            return dictionary.split("\\r?\\n");
        } catch (IOException e) {
            throw new ApplicationException("Error during reading dictionary file. Check correctness of name and permissions.", e);
        }
    }

    public static void writeFile(String filename, List<Map.Entry<String, Integer>> calculatedData) {
        log.log(Level.INFO, "Writing file: " + filename);
        try {
            FileUtils.writeLines(new File(filename), ENCODING_UTF_8, calculatedData);
        } catch (IOException e) {
            throw new ApplicationException("Error during writing result file. Check correctness of name and permissions.", e);
        }
    }

    public static String[] parseWords(String fileLine) {
        return fileLine.split("[\\W]");
    }

    public static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map,
                                                                  Comparator<Map.Entry<String, Integer>> comparator) {
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(comparator);
        List<Map.Entry<String, Integer>> valuesList = new LinkedList<>(map.entrySet());
        Collections.sort(valuesList, comparator);
        return Collections.unmodifiableList(valuesList);
    }
}
