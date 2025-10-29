/*package HomeworkAssignment1.picking;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Task;


public class PickingTask extends Task {

    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the generic Task

    public PickingTask(Order order) {
        super(order);
    }
}
 */
package HomeworkAssignment1.picking;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Task;

public class PickingTask extends Task {

    private final String shelfLocation;
    private final String pickerName;
    private final boolean itemAvailable;

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

    public boolean isItemAvailable() {
        return itemAvailable;
    }
}
