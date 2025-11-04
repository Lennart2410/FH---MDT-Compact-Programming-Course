package HomeworkAssignment3.picking;

import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Task;

/**
 * PickingTask represents a task assigned to a picker to retrieve an item from a
 * shelf.
 */
public class PickingTask extends Task {

    private final String shelfLocation;
    private boolean itemAvailable;
    private String pickerName; //

    public PickingTask(Order order, String shelfLocation, String pickerName, boolean itemAvailable) {
        super(order);
        this.shelfLocation = shelfLocation;
        this.pickerName = pickerName;
        this.itemAvailable = itemAvailable;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public String getPickerName() {
        return pickerName;
    }

    public void setPickerName(String pickerName) {
        this.pickerName = pickerName;
    }

    public boolean isItemAvailable() {
        return itemAvailable;
    }

    public void setItemAvailable(boolean itemAvailable) {
        this.itemAvailable = itemAvailable;
    }
}