package HomeworkAssignment3;

import HomeworkAssignment3.logging.LogFiles;

import java.nio.file.Path;
import java.time.LocalDate;

public class WarehouseRunner {
    public static void main(String[] args) {
        try {
            // Initialize logger
            LogFiles logger = new LogFiles(Path.of("logs"));

            // Create log file path
            Path logFile = logger.pathFor("WarehouseRunner", null, LocalDate.now());

            // Write startup log entry
            logger.appendLine(logFile, logger.line("WarehouseRunner", "startup", "System initialized"));

            // Start warehouse system
            new Warehouse();

        } catch (Exception e) {
            e.printStackTrace(); // Optional: log to console or fallback log
        }
    }
}