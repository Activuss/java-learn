package ua.od.fabrika;

import java.io.*;
import java.util.Scanner;

public class FileToStringReader {

    private static File getFileFromUser() {
        boolean isFileExists = false;
        File textFile = null;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input correct path to file");

        while (!isFileExists && scanner.hasNext()) {
            String path = scanner.nextLine();
            textFile = new File(path);
            if (textFile.exists() && !textFile.isDirectory()) {
                System.out.println("File was founded");
                isFileExists = true;
            } else {
                System.out.println("Unfortunately there is no such a file. Try again.");
            }
        }
        return textFile;
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(getFileFromUser()))){
            String fileLine;
            StringBuilder stringBuilder = new StringBuilder();

            System.out.println("Start reading file...");

            while ((fileLine = reader.readLine()) != null) {
                stringBuilder.append(fileLine);
                stringBuilder.append("\n");
            }

            System.out.println(stringBuilder.toString());

        } catch (IOException e) {
            System.out.println("Some problem during reading from file: " + e.getMessage() + " " + e.getCause());
        }
    }
}
