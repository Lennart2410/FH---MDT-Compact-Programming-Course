package HomeworkAssignment3.agv;

public class AGV {
    private final String id;
    private boolean isBusy;
    private double energyLevel;

    public AGV(String id) {
        this.id = id;
        this.isBusy = false;
        energyLevel = 100.0;
    }

    public String getId() {
        return id;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public double getEnergyLevel() {
        return energyLevel;
    }
    public void setEnergyLevel(double energyLevel) {
        this.energyLevel = energyLevel;
    }

    public void setBusy(boolean busy) {
        this.isBusy = busy;
    }

}
