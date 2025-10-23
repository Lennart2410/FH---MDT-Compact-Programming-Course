package CapstoneProject;

import java.util.ArrayList;
import java.util.List;

public class Order {



    private List<Item> items = new ArrayList<Item>();
    private String destination;
    private String orderNumber;

    public Order(String destination, List<Item> items) {
        this.destination = destination;
        this.items = items;
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
}
