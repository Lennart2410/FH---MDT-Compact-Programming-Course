package HomeworkAssignment3.picking;


import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Task;

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
