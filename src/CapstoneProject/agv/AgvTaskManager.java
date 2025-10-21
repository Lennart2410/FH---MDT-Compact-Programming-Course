package CapstoneProject.agv;

import CapstoneProject.Warehouse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgvTaskManager implements Runnable{

    private final BlockingQueue<AgvTask> queue;
    private final Warehouse warehouse;

    public AgvTaskManager(Warehouse warehouse) {
        this.queue = new LinkedBlockingQueue<>();
        this.warehouse = warehouse;
    }

    public void travelWithItems(AgvTask agvTask) {
        System.out.println("["+ getCurrentTime()+"] - "+"[AGV]: Traveling with items! Going total distance of " + agvTask.getTravelDistanceMeter() + "m. It takes " + (agvTask.getTaskDurationMS() / 1000) + " seconds!");
        try {
            Thread.sleep(agvTask.getTaskDurationMS());

            // Do something specific for traveling with items here
            // At the end queue a new WorkstationTask into the Workstation-Queue.
            System.out.println("["+ getCurrentTime()+"] - "+"[AGV]: Reached destination with current loaded items!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        AgvTask agvTask = queue.poll();
        if (agvTask != null) {
            // Here we can add multiple Threads (Depeding on how many workers we currently have)
            travelWithItems(agvTask);
        } else {
            System.out.println("["+ getCurrentTime()+"] - "+"[AGV]: No current jobs!");
        }
    }

    public void putItemIntoQueue(AgvTask agvTask) {
        queue.add(agvTask);
    }

    private String getCurrentTime(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
