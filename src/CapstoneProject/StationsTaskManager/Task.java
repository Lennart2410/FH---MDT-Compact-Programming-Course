package CapstoneProject.StationsTaskManager;

/** Immutable message that moves between stations via queues. */

public class Task {
    private final String id;        // unique job id (not the order number)
    private final String orderNo;   //  (e.g., "A100")
    private final Status status;

    private final Object extraTaskData;

    /** Create a new task at a given status */
    public Task(String id, String orderNo, Status status, Object extraTaskData) {
        this.id = id;
        this.orderNo = orderNo;
        this.status = status;
        this.extraTaskData = extraTaskData;
    }


    public String getId()     { return id; }
    public String getOrderNo(){ return orderNo; }
    public Status getStatus()   { return status; }
    public Task withStatus(Status status){ return new Task(id, orderNo, status, extraTaskData); }
    public Task withExtraData(Object extraData){ return new Task(id, orderNo, status, extraData); }

    public Object getExtraTaskData() {
        return extraTaskData;
    }


    @Override public String toString() {
        return "Task{" + id + ", order=" + orderNo + ", Status=" + status + "}";
    }
}
