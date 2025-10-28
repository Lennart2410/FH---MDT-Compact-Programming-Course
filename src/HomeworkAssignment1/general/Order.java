package HomeworkAssignment1.general;

import HomeworkAssignment1.packing.Parcel;

import java.util.List;
import java.util.UUID;

public class Order {


    private final List<Item> items;
    private final String destination;
    private final String orderNumber;
    private  OrderStatusEnum orderStatusEnum;
    private List<Parcel> orderParcels = null;
    private double totalWeight;
    public void setOrderParcels(List<Parcel> orderParcels) {
        this.orderParcels = orderParcels;
    }


    public Order(String destination, List<Item> items) {
        this.orderStatusEnum = OrderStatusEnum.ORDERED;
        this.orderNumber = "ORD00" + UUID.randomUUID();
        this.destination = destination;
        this.items = items;
        this.totalWeight = 12;
    }

    public String toString() {
        return "Order [items=" + items + ", orderNumber=" + orderNumber + ", destination=" + destination + "]";
    }
    public String getOrderNumber() {
        return orderNumber;
    }

    public List<Parcel> getOrderParcels() {
        return orderParcels;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }
    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
