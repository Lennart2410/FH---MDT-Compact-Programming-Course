
package HomeworkAssignment3.agv;

import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Task;

public class AgvTask extends Task {
    private final String startingLocation;
    private final String destinationLocation;
    private String agvId;

    // Constructor with agvId (used when you already know which AGV will handle the
    // task)
    public AgvTask(Order order, String startingLocation, String destinationLocation, String agvId) {
        super(order);
        this.startingLocation = startingLocation;
        this.destinationLocation = destinationLocation;
        this.agvId = agvId;
    }

    // constructor without agvId (used when AGV is assigned later)
    public AgvTask(Order order, String startingLocation, String destinationLocation) {
        super(order);
        this.startingLocation = startingLocation;
        this.destinationLocation = destinationLocation;
        this.agvId = null;
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

    public void setAgvId(String agvId) {
        this.agvId = agvId;
    }
}