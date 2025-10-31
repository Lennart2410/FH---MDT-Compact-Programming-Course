package HomeworkAssignment2.agv;

import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.general.Task;

public class AgvTask extends Task {
    private final String startingLocation;
    private final String destinationLocation;
    private final String agvId;

    public AgvTask(Order order, String startingLocation, String destinationLocation, String agvId) {
        super(order);
        this.startingLocation = startingLocation;
        this.destinationLocation = destinationLocation;
        this.agvId = agvId;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public String getAgvId() {
        return agvId;
    }
}
