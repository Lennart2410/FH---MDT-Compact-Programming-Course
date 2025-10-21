package CapstoneProject.shelve;

import CapstoneProject.Warehouse;
import CapstoneProject.agv.AgvTask;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class ShelveTaskManager implements Runnable {

    private final BlockingQueue<ShelveTask> queue;
    private final Warehouse warehouse;

    public ShelveTaskManager(Warehouse warehouse) {
        this.warehouse = warehouse;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void putItemsIntoAGV(ShelveTask shelveTask) {
        System.out.println("["+ getCurrentTime()+"] - "+"[Shelve]: Putting things from shelves into agv's! Retrieving from shelve nr. " + shelveTask.getShelveNumber() + ". It takes " + (shelveTask.getTaskDurationMS() / 1000) + " seconds!");
        try {
            Thread.sleep(shelveTask.getTaskDurationMS());
            // Do something specific for retrieving items from shelves here
            AgvTask agvTask = new AgvTask();
            warehouse.getAgvTaskManager().putItemIntoQueue(agvTask);
            System.out.println("["+ getCurrentTime()+"] - "+"[Shelve]: Completed putting items into agv and started a new AGV-Task!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        ShelveTask shelveTask = queue.poll();
        if (shelveTask != null) {
            // Here we can add multiple Threads (Depeding on how many workers we currently have)
            putItemsIntoAGV(shelveTask);
        } else {
            System.out.println("["+ getCurrentTime()+"] - "+"[Shelve]: No current jobs!");
        }
    }

    public void putItemIntoQueue(ShelveTask shelveTask) {
        queue.add(shelveTask);
    }

    private String getCurrentTime(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
