package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Task;

public class PackingTask extends Task {
    private final BoxingService boxing;
    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the generic Task

    public PackingTask(Order order, BoxingService boxing) {
        super(order);
        this.boxing = boxing;
    }

    public BoxingService getBoxing() {
        return boxing;
    }
}
