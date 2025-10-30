package HomeworkAssignment1.picking;

/**
 * PickingStation simulates the cooperation between AGVs and employees
 * during the item picking process. It logs each step and handles errors
 * such as unavailable items using custom exceptions.
 */

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Station;
import HomeworkAssignment1.logging.LogFiles;

import java.nio.file.Path;
import java.time.LocalDate;

public class PickingStation extends Station<PickingTask> {

    // Logger initialized with base directory "logs"
    private final LogFiles logger = new LogFiles(Path.of("logs"));

    @Override
    public Order process(PickingTask task) {
        try {
            // Create log file path for today's picking session
            Path logFile = logger.pathFor("picking", "PickingStation", LocalDate.now());

            // Log start of task
            logger.appendLine(logFile, logger.line("PickingStation", "started",
                    "Order " + task.getOrder().toString()));

            // Check item availability
            if (!task.isItemAvailable()) {
                logger.appendLine(logFile, logger.line("PickingStation", "error",
                        "Item not available at shelf " + task.getShelfLocation()));
                throw new ItemNotFoundException("Item not available at shelf " + task.getShelfLocation());
            }

            // Simulate picking time
            Thread.sleep(15000);

            // Log successful completion
            logger.appendLine(logFile, logger.line("PickingStation", "finished",
                    "Picked by " + task.getPickerName()));

            return task.getOrder();

        } catch (ItemNotFoundException e) {
            // Log and rethrow as custom picking exception
            throw new PickingException("Picking failed", e);
        } catch (Exception e) {
            // Catch unexpected errors
            throw new RuntimeException("Unexpected error in PickingStation", e);
        }
    }
}