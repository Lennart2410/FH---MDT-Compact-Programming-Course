package HomeworkAssignment2.picking;

import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.general.Station;

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
