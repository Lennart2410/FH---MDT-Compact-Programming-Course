package CapstoneProject.agv;

import java.util.Random;

public class AgvTask {

    private final int travelDistanceMeter;
    private final int taskDurationMS;


    public AgvTask() {
        // Just a random generated number for distance: Something between 5 and 15 seconds
        this.travelDistanceMeter = new Random().nextInt(10) + 5;
        this.taskDurationMS = this.travelDistanceMeter * 1000;
    }

    public int getTravelDistanceMeter() {
        return travelDistanceMeter;
    }

    public int getTaskDurationMS() {
        return taskDurationMS;
    }
}
