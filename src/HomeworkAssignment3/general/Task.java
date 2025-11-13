package HomeworkAssignment3.general;


import java.io.Serializable;
import java.util.UUID;

/**
 * Immutable message that moves between stations via queues.
 */

public abstract class Task implements Serializable {
    private final String id;
    private final Order order;



    /**
     * Create a new task at a given status
     */
    public Task(Order order) {
        this.id = "TSK00"+ UUID.randomUUID();
        this.order = order;
    }

    public Task(Order order, String id) {
        this.id = id;
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
