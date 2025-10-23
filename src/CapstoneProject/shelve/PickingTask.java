package CapstoneProject.shelve;

import CapstoneProject.Order;

public class PickingTask {

    private final int shelveNumber;
    private final int taskDurationMS;
    private Order order;


    public PickingTask(int shelveNumber, int taskDurationMS, Order order) {
        this.shelveNumber = shelveNumber;
        this.taskDurationMS = taskDurationMS;
        this.order = order;
    }

    public int getShelveNumber() {
        return shelveNumber;
    }

    public int getTaskDurationMS() {
        return taskDurationMS;
    }

    public Order getOrder() {
        return order;
    }
}
