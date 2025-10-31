package HomeworkAssignment2.general;

import java.util.UUID;

/**
 * Immutable message that moves between stations via queues.
 */

public abstract class Task {
    private final String id;
    private final Order order;



    /**
     * Create a new task at a given status
     */
    public Task(Order order) {
        this.id = "TSK00"+UUID.randomUUID();
        this.order = order;
    }


    public String getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }


    @Override
    public String toString() {
        return "Task[" + id + ", order=" + order.toString()+"]";
    }
}
