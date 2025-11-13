package CapstoneProject.agv.energyloading;

import java.util.UUID;

public class LoadingSlot {
    private final String id;
    private boolean occupation;
    private double loadingSpeedPerMinute;

    public LoadingSlot() {
        this.id = UUID.randomUUID().toString();
        this.occupation = false;
        this.loadingSpeedPerMinute = 3;
    }

    public LoadingSlot(double loadingSpeedPerMinute) {
        this.id = UUID.randomUUID().toString();
        this.occupation = false;
        this.loadingSpeedPerMinute = loadingSpeedPerMinute;
    }

    public String getId() {
        return id;
    }

    public boolean isOccupation() {
        return occupation;
    }

    public double getLoadingSpeedPerMinute() {
        return loadingSpeedPerMinute;
    }

    public void setOccupation(boolean occupation) {
        this.occupation = occupation;
    }

    public void setLoadingSpeedPerMinute(double loadingSpeedPerMinute) {
        this.loadingSpeedPerMinute = loadingSpeedPerMinute;
    }
}
