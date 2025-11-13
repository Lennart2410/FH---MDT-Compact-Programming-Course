package CapstoneProject.picking;

import CapstoneProject.agv.AgvTask;
import CapstoneProject.general.*;
import CapstoneProject.general.exceptions.WarehouseException;
import CapstoneProject.picking.exceptions.ItemNotFoundException;
import CapstoneProject.picking.exceptions.PickingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * PickingStation simulates the cooperation between AGVs and employees
 * during the item picking process. It logs each step and handles errors
 * such as unavailable items using custom exceptions.
 */
public class PickingStation extends Station<PickingTask> {

    // List of available pickers
    private final List<Employee> pickers = new ArrayList<>();


    public PickingStation(BlockingQueue<Task> in, BlockingQueue<Task> out, OrderStatusListener listener) {
        super(in, out,listener);
        pickers.add(new Employee("Alice", 28, JobType.PICKER));
        pickers.add(new Employee("Bob", 32, JobType.PICKER));
        pickers.add(new Employee("Charlie", 25, JobType.PICKER));
    }

    @Override
    public void process(PickingTask task) throws WarehouseException {
        System.out.println("PickingStation received a task.");
        new Thread(() -> {
            try {
                logManager.writeLogEntry("A LoadingTask with the order number " + task.getOrder().getOrderNumber() + " should be processed.", "Warehouse");
                logManager.writeLogEntry("New Task was received by the picking station. Task will be done by picker " + task.getPicker().getName(), "PickingStation");
                task.getPicker().setCurrentlyOccupied(true);

                logManager.writeLogEntry("Started picking of order " + task.getOrder().getOrderNumber(), "PickingStation");

                if (!task.isItemAvailable()) {
                    task.getOrder().setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
                    getListener().onOrderStatusChanged(task.getOrder());
                    logManager.writeLogEntry("ERROR: Item not available at shelf " + task.getShelfLocation(), "PickingStation");
                    throw new ItemNotFoundException("Item not available at shelf " + task.getShelfLocation());
                }

                Thread.sleep(10000); // Simulate picking time


                logManager.writeLogEntry("Finished picking order by " + task.getPicker().getName(), "PickingStation");

                task.getOrder().setOrderStatusEnum(OrderStatusEnum.PICKED);
                getListener().onOrderStatusChanged(task.getOrder());
                System.out.println("PickingStation finished picking. Putting task into AGV queue.");
                addToQueue(new AgvTask(task.getOrder(), "shelves", "packing-station"));

            } catch (PickingException e) {
                logManager.writeLogEntry("ERROR: There waw a problem at picking items.", "PickingStation");
                throw new RuntimeException("Picking failed: " + e.getMessage(), e);

            } catch (Exception e) {
                logManager.writeLogEntry("ERROR: There waw a problem at picking items.", "PickingStation");
                throw new RuntimeException("Unexpected error in PickingStation", e);

            } finally {
                logManager.writeLogEntry("Task was finished. Picker " + task.getPicker().getName() + " is available again.", "PickingStation");
                task.getPicker().setCurrentlyOccupied(false);
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
                    task.setPicker(picker);
                    // start the concrete process
                    process(task);
                    // when the process / thread is finished, set the picker to unassigned.
                } else {
                    System.out.println("No available picker. Task will wait.");
                    logManager.writeLogEntry("WARN: No picker employee was available. Task will wait for availability.", "PickingStation");
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