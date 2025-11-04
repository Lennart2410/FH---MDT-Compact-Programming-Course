package HomeworkAssignment3.picking;

import HomeworkAssignment3.agv.AgvTask;
import HomeworkAssignment3.general.Employee;
import HomeworkAssignment3.general.JobType;
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
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * PickingStation simulates the cooperation between AGVs and employees
 * during the item picking process. It logs each step and handles errors
 * such as unavailable items using custom exceptions.
 */
public class PickingStation extends Station<PickingTask> {

    private final LogFiles logger = new LogFiles(Path.of("logs"));

    // List of available pickers
    private final List<Employee> pickers = List.of(
            new Employee("Alice", 28, JobType.PICKER),
            new Employee("Bob", 32, JobType.PICKER),
            new Employee("Charlie", 25, JobType.PICKER));

    public PickingStation(BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in, out);
    }

    @Override
    public void process(PickingTask task) throws WarehouseException {
        System.out.println("PickingStation received a task.");

        new Thread(() -> {
            try {
                Path logFile = logger.pathFor("PickingStation", null, LocalDate.now());
                logger.appendLine(logFile, logger.line("PickingStation", "started",
                        "Order " + task.getOrder().toString()));

                if (!task.isItemAvailable()) {
                    logger.appendLine(logFile, logger.line("PickingStation", "error",
                            "Item not available at shelf " + task.getShelfLocation()));
                    throw new ItemNotFoundException("Item not available at shelf " + task.getShelfLocation());
                }

                Thread.sleep(15000); // Simulate picking time

                logger.appendLine(logFile, logger.line("PickingStation", "finished",
                        "Picked by " + task.getPickerName()));

                task.getOrder().setOrderStatusEnum(OrderStatusEnum.PICKED);
                System.out.println("PickingStation finished picking. Putting task into AGV queue.");
                addToQueue(new AgvTask(task.getOrder(), "shelves", "packing-station", "AGV-01"));

            } catch (PickingException e) {
                try {
                    Path logFile = logger.pathFor("PickingStation", null, LocalDate.now());
                    logger.appendLine(logFile, logger.line("PickingStation", "finished",
                            "Picked by " + task.getPickerName()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException("Picking failed: " + e.getMessage(), e);

            } catch (Exception e) {
                throw new RuntimeException("Unexpected error in PickingStation", e);

            }
        }).start();

    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // checks if a picker is currently available for the task
                Employee picker = retrievePossiblePicker();
                if (picker != null) {
                    // If picker is available
                    PickingTask task = (PickingTask) in.take();
                    picker.setCurrentlyOccupied(true);
                    task.setPickerName(picker.getName());
                    // start the concrete process
                    process(task);
                    // when the process / thread is finished, set the picker to unassigned.
                    picker.setCurrentlyOccupied(false);
                } else {
                    System.out.println("No available picker. Task will wait.");
                }
            }
        } catch (InterruptedException stop) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // As a safety net, if a station throws, convert task to EXCEPTION
            e.printStackTrace();
        }
    }

    public Employee retrievePossiblePicker() {
        for (Employee picker : pickers) {
            if (picker.getJobType() == JobType.PICKER && !picker.isCurrentlyOccupied()) {
                return picker;
            }
        }
        return null;
    }
}