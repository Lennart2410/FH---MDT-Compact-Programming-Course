package CapstoneProject.shelve;

public class ShelveTask {

    private final int shelveNumber;
    private final int taskDurationMS;


    public ShelveTask(int shelveNumber, int taskDurationMS) {
        this.shelveNumber = shelveNumber;
        this.taskDurationMS = taskDurationMS;
    }

    public int getShelveNumber() {
        return shelveNumber;
    }

    public int getTaskDurationMS() {
        return taskDurationMS;
    }
}
