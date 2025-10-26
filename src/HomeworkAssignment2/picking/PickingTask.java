package HomeworkAssignment2.picking;

import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.general.Task;


public class PickingTask extends Task {

    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the generic Task

    public PickingTask(Order order) {
        super(order);
    }
}
