package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.OrderStatusEnum;
import HomeworkAssignment1.general.Station;

public class PackingStation extends Station<PackingTask> {

    @Override
    public Order process(PackingTask packingTask) {
        // Packing items from the order
        // Do something that the Packing-process would usually cover
        // Process should take some seconds to make it realistic

        System.out.println("Im im packing!");
        var parcels = packingTask.getBoxing().cartonize();
        System.out.println(parcels.toString());
        packingTask.getOrder().setOrderParcels(parcels);
        packingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.PACKAGING);
        return packingTask.getOrder();
    }

}
