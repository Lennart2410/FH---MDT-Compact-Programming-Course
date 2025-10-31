package HomeworkAssignment2.loading.vehicles;

import HomeworkAssignment2.packing.Parcel;

import java.util.ArrayList;
import java.util.List;

public abstract class Car {

    private String id;
    private final double totalCapacity;
    private final double currentCapacity;
    private String desination;
    private final List<Parcel> parcelList;
    private static int globalCarId = 0;

    public Car(double totalCapacity, String destination) {
        id = "Car" + String.format("%05d", globalCarId++);
        this.totalCapacity = totalCapacity;
        this.desination = destination;
        this.currentCapacity = 0;
        this.parcelList = new ArrayList<>();
    }

    public void drive() {

    }

    public String getId() {
        return id;
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

    public double getCurrentCapacity() {
        return currentCapacity;
    }

    public List<Parcel> getParcelList() {
        return parcelList;
    }

    public String getDesination() {
        return desination;
    }

    public void setDesination(String desination) {
        this.desination = desination;
    }

    public void addParcel(Parcel parcel) {
        parcelList.add(parcel);
    }
}
