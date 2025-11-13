package CapstoneProject.picking;

import CapstoneProject.general.Employee;
import CapstoneProject.general.Order;
import CapstoneProject.general.Task;

/**
 * PickingTask represents a task assigned to a picker to retrieve an item from a
 * shelf.
 */
public class PickingTask extends Task {

    private final String shelfLocation;
    private boolean itemAvailable;
    private Employee picker; //

    public PickingTask(Order order, String shelfLocation, boolean itemAvailable) {
        super(order);
        this.shelfLocation = shelfLocation;
        this.itemAvailable = itemAvailable;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public Employee getPicker() {
        return picker;
    }

    public void setPicker(Employee picker) {
        this.picker = picker;
    }

    public boolean isItemAvailable() {
        return itemAvailable;
    }

    public void setItemAvailable(boolean itemAvailable) {
        this.itemAvailable = itemAvailable;
    }
}