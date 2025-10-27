package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.OrderStatusEnum;

import java.util.List;

public class OrderBoxingService implements BoxingService{
    private final Order order;
    public OrderBoxingService(Order order) {
        this.order = order;
    }

    @Override
    public List<String> cartonize() {
        double w = order.getTotalWeight();
        String box = w <= 1.0 ? "S" : (w <= 10.0 ? "M" : "L");
        double parcelW = w + 0.3;
        return List.of("Parcel[" + box + "," + String.format("%.1f", parcelW) + "kg]");
    }
}
