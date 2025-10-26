package HomeworkAssignment2.packing;

import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.general.Station;

public class PackingStation extends Station<PackingTask> {

    @Override
    public Order process(PackingTask packingTask) {
        // Packing items from the order
        // Do something that the Packing-process would usually cover
        // Process should take some seconds to make it realistic

        System.out.println("Im im packing!");

        return packingTask.getOrder();
    }

}
