package CapstoneProject.agv;

import CapstoneProject.Order;
import CapstoneProject.Task;

import java.util.Random;

public class AgvTask {

    private final int travelDistanceMeter;
    private final int taskDurationMS;
    private Order order;


    public AgvTask(Order order) {
        // Just a random generated number for distance: Something between 5 and 15 seconds
        this.travelDistanceMeter = new Random().nextInt(10) + 5;
        this.taskDurationMS = this.travelDistanceMeter * 1000;
        this.order = order;
    }

    public int getTravelDistanceMeter() {
        return travelDistanceMeter;
    }

    public int getTaskDurationMS() {
        return taskDurationMS;
    }
}
