package CapstoneProject.agv.energyloading;

import CapstoneProject.agv.exceptions.EnergyStationException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AGVEnergyStation {
    private final String id;
    private List<LoadingSlot> loadingSlotList;
    private final int capacity;

    public AGVEnergyStation(){
        id = UUID.randomUUID().toString();
        loadingSlotList = new ArrayList<LoadingSlot>();

        // Standard capacity of 2 loading areas for agvs
        capacity = 2;
    }

    public AGVEnergyStation(int capacity){
        id = UUID.randomUUID().toString();
        loadingSlotList = new ArrayList<>();
        this.capacity = capacity;
    }

    public void addLoadingSlot(LoadingSlot loadingSlot) throws EnergyStationException {
        if(loadingSlotList.size() < capacity){
            loadingSlotList.add(loadingSlot);
        }
        else {
            throw new EnergyStationException("There is no more space for a loading slot at the agv energy station with id: " + id);
        }
    }

    public List<LoadingSlot> getLoadingSlotList() {
        return loadingSlotList;
    }

    public int getCapacity() {
        return capacity;
    }
}
