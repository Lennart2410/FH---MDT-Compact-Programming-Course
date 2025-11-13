package CapstoneProject.general;

import java.io.Serializable;

public class Item implements Serializable {


    private final String itemName;

    public Item(String itemName) {
        this.itemName = itemName;
    }


    public String toString() {
        return "Item [itemname=" + itemName + "]";
    }
}
