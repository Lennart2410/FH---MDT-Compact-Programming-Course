package HomeworkAssignment3.picking;

import HomeworkAssignment3.agv.AgvTask;
import HomeworkAssignment3.general.OrderStatusEnum;
import HomeworkAssignment3.general.Station;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.logging.LogFiles;
import HomeworkAssignment3.picking.exceptions.ItemNotFoundException;
import HomeworkAssignment3.picking.exceptions.PickingException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;

/**
 * PickingStation simulates the cooperation between AGVs and employees
 * during the item picking process. It logs each step and handles errors
 * such as unavailable items using custom exceptions.
 */



public class PickingStation extends Station<PickingTask> {

    public PickingStation(BlockingQueue<Task> in, BlockingQueue<Task> out){
        super(in, out);
    }

    // Logger initialized with base directory "logs"
    private final LogFiles logger = new LogFiles(Path.of("logs"));

    @Override
    public void process(PickingTask task) throws WarehouseException {
        System.out.println("PickingStation starts picking.");
        try {
            Path logFile = logger.pathFor("picking", "PickingStation", LocalDate.now());
            // Create log file path for today's picking session

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


        } catch (PickingException e) {
            try {
                Path logFile = logger.pathFor("picking", "PickingStation", LocalDate.now());
                logger.appendLine(logFile, logger.line("PickingStation", "finished",
                        "Picked by " + task.getPickerName()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // After logging the error, rethrow a generic WarehouseException for the warehouse system
            throw new WarehouseException("A failure inside the warehouse picking system occurred.", e);
        } catch (Exception e) {
            // Catch unexpected errors
            throw new RuntimeException("Unexpected error in PickingStation", e);
        }
        task.getOrder().setOrderStatusEnum(OrderStatusEnum.PICKED);
        System.out.println("PickingStation finished picking. Putting task into agv queue.");
        addToQueue(new AgvTask(task.getOrder(), "shelves", "packing-station", "AGV-01"));
    }
}