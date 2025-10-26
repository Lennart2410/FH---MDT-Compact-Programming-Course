package HomeworkAssignment1.general;

import java.util.List;
import java.util.UUID;

public class Order {


    private final List<Item> items;
    private final String destination;
    private final String orderNumber;
    private final OrderStatusEnum orderStatusEnum;

    public Order(String destination, List<Item> items) {
        this.orderStatusEnum = OrderStatusEnum.ORDERED;
        this.orderNumber = "ORD00" + UUID.randomUUID();
        this.destination = destination;
        this.items = items;
    }

    public String toString() {
        return "Order [items=" + items + ", orderNumber=" + orderNumber + ", destination=" + destination + "]";
    }
}
