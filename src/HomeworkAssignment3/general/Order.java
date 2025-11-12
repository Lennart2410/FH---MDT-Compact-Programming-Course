package HomeworkAssignment3.general;

import HomeworkAssignment3.packing.Parcel;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Order {

    private static int orderCounter = 1;

    private final List<Item> items;
    private final String destination;
    private final String orderNumber;
    private OrderStatusEnum orderStatusEnum;
    private List<Parcel> orderParcels = null;
    private double totalWeight;
    public void setOrderParcels(List<Parcel> orderParcels) {
        this.orderParcels = orderParcels;
    }


    public Order(String destination, List<Item> items) {
        this.orderStatusEnum = OrderStatusEnum.ORDERED;
        this.orderNumber = "ORD-" + String.format("%05d", orderCounter++);
        this.destination = destination;
        this.items = items;
        this.totalWeight = 12;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public void createFile(){

        System.out.println("Order_"+ orderNumber + "_" + LocalDate.now()+".txt");
    }


    public List<Parcel> getOrderParcels() {
        return orderParcels;
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

    public String toString() {
        return "Order [items=" + items + ", orderNumber=" + orderNumber + ", destination=" + destination + "]";
    }
}
