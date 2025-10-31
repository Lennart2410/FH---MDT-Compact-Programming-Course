package HomeworkAssignment2.packing;

import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.packing.BoxingService;
import HomeworkAssignment2.packing.Parcel;

import java.util.List;

public class OrderBoxingService implements BoxingService {
    private final Order order;
    public OrderBoxingService(Order order) {
        this.order = order;
    }

    @Override
    public List<Parcel> cartonize() {
        double w = order.getTotalWeight();
        String box = w<=1?"S":(w<=10?"M":"L");
        return List.of(new Parcel("P-"+order.getOrderNumber(), box, w+0.3));
    }
}
