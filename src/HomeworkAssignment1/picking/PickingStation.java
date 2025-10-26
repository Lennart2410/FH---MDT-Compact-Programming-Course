package HomeworkAssignment1.picking;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Station;

public class PickingStation extends Station<PickingTask> {

    @Override
    public Order process(PickingTask pickingTask) {
        // Pick items from the order
        // Do something that the Picking-process would usually cover
        // Process should take some seconds to make it realistic

        System.out.println("Im im picking!");

        return pickingTask.getOrder();
    }
}
