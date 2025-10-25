package system;

import java.io.*;
import java.util.regex.*;

public class LogManager {
    public void findLog(String keywordOrDate) {
        File folder = new File("logs");
        Pattern pattern = Pattern.compile(".*" + keywordOrDate + ".*\\.log");

        File[] files = folder.listFiles();
        if (files == null) {
            System.out.println("No log files found.");
            return;
        }

        for (File file : files) {
            Matcher matcher = pattern.matcher(file.getName());
            if (matcher.matches()) {
                System.out.println("Found log: " + file.getName());
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading log: " + e.getMessage());
                }
            }
        }
    }
}
