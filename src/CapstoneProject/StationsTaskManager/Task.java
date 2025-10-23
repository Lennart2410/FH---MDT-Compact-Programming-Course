package CapstoneProject.StationsTaskManager;

import java.util.UUID;

/** Immutable message that moves between stations via queues. */

public class Task {
    private final String id;        // unique job id (not the order number)
    private final String orderNo;   // the business id (e.g., "A100")
    private final OrderStatus orderStatus;      // current lifecycle stage

    /** Create a new task at a given stage */
    public Task(String id, String orderNo, OrderStatus orderStatus) {
        this.id = id;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
    }

    /** Convenience factory for a fresh Task (id auto-generated). */
    public static Task fresh(String orderNo) {
        return new Task(UUID.randomUUID().toString(), orderNo, OrderStatus.ORDERED);
    }

    public String getId()     { return id; }
    public String getOrderNo(){ return orderNo; }
    public OrderStatus getOrderStatus()   { return orderStatus; }



    @Override public String toString() {
        return "Task{" + id + ", order=" + orderNo + ", stage=" + orderStatus + "}";
    }
}
