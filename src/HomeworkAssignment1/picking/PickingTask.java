package HomeworkAssignment1.picking;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Task;


public class PickingTask extends Task {

    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the generic Task

    public PickingTask(Order order) {
        super(order);
    }
}
