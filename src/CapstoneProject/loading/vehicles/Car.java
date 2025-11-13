package CapstoneProject.loading.vehicles;

import CapstoneProject.packing.Parcel;

import java.util.ArrayList;
import java.util.List;

public abstract class Car {

    private String id;
    private final double totalCapacity;
    private final double currentCapacity;
    private String desination;
    private final List<Parcel> parcelList;
    private static int globalCarId = 0;
    private  boolean currentlyDelivering;

    public Car(double totalCapacity, String destination) {
        id = "Car" + String.format("%05d", globalCarId++);
        this.totalCapacity = totalCapacity;
        this.desination = destination;
        this.currentCapacity = totalCapacity;
        this.parcelList = new ArrayList<>();
        this.currentlyDelivering = false;
    }

    public void drive() {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public boolean isCurrentlyDelivering() {
        return currentlyDelivering;
    }

    public void setCurrentlyDelivering(boolean currentlyDelivering) {
        this.currentlyDelivering = currentlyDelivering;
    }
}
