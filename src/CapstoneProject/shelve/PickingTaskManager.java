package CapstoneProject.shelve;

import CapstoneProject.Item;
import CapstoneProject.Warehouse;
import CapstoneProject.agv.AgvTask;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.*;

public class PickingTaskManager implements Runnable {

    private final BlockingQueue<PickingTask> queue;
    private final Warehouse warehouse;

    public PickingTaskManager(Warehouse warehouse) {
        this.warehouse = warehouse;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void pickingItemsForOrder(PickingTask pickingTask) {
        System.out.println("["+ getCurrentTime()+"] - "+"[Shelve]: Picking items for an order! Retrieving from shelve nr. " + pickingTask.getShelveNumber() + ". Order contains " + pickingTask.getOrder().getItems().size() + " items!");
        try {
            Thread.sleep(pickingTask.getTaskDurationMS());

            for (Item item : pickingTask.getOrder().getItems()) {
                System.out.println("Picking item with name: " + item + " from the shelve");
                // Do something between 1 and 5 Seconds
                Thread.sleep((new Random().nextInt(4) + 1)*1000);
            }


            // Do something specific for retrieving items from shelves here
            AgvTask agvTask = new AgvTask(pickingTask.getOrder());


            warehouse.getAgvTaskManager().putItemIntoQueue(agvTask);

            System.out.println("["+ getCurrentTime()+"] - "+"[Shelve]: Completed picking items and started a new AGV-Task!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        PickingTask pickingTask = queue.poll();
        if (pickingTask != null) {
            // Here we can add multiple Threads (Depeding on how many workers we currently have)
            pickingItemsForOrder(pickingTask);
        } else {
            System.out.println("["+ getCurrentTime()+"] - "+"[Shelve]: No current jobs!");
        }
    }

    public void putItemIntoQueue(PickingTask pickingTask) {
        queue.add(pickingTask);
    }

    private String getCurrentTime(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
