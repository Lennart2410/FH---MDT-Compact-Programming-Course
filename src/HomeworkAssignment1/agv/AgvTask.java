package HomeworkAssignment1.agv;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Task;

public class AgvTask extends Task {
    String startingLocation;
    String destinationLocation;

    // Specific content of the AgvTask
    // Should contain attributes which only accounts to the AgvTask and not the generic Task
    // For example startingLocation and destinationLocation should always be present

    public AgvTask(Order order, String startingLocation, String destinationLocation) {
        super(order);
        this.startingLocation = startingLocation;
        this.destinationLocation = destinationLocation;
    }
}
