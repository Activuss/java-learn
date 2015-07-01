import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static final String ENCODING_UTF_8 = "UTF-8";
    private static final Logger log = Logger.getLogger(Util.class.getName());

    public static List<String> readFile (String filename) throws IOException {
        log.log(Level.INFO, "Reading file");
        return FileUtils.readLines(new File(filename), ENCODING_UTF_8);
    }

    public static void writeFile (String filename, List<Map.Entry<String, Integer>> calculatedData)
            throws IOException {
        log.log(Level.INFO, "Writing file");
        FileUtils.writeLines(new File(filename), ENCODING_UTF_8, calculatedData);
    }

    public static String[] parseWords (String fileLine) {
        return fileLine.split("[\\W]");
    }

    public static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map,
                                                Comparator<Map.Entry<String, Integer>> comparator) {
        List<Map.Entry<String, Integer>> valuesList = new LinkedList<>(map.entrySet());
        Collections.sort(valuesList, comparator);

        return Collections.unmodifiableList(valuesList);
    }
}
