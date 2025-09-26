package SelfAssignment2;

public class AGV {
    private String id;
    private double batteryLoad;
    private double consumption;
    private double chargingTimeInMinutes;
    private Position position;
    private float maxSpeed;
    private float actSpeed;

    public AGV(String id, double batteryLoad, double consumption, Position position, double chargingTimeInMinutes, float maxSpeed, float actSpeed) {
        this.id = id;
        this.batteryLoad = batteryLoad;
        this.consumption = consumption;
        this.position = position;
        this.chargingTimeInMinutes = chargingTimeInMinutes;
        this.maxSpeed = maxSpeed;
        this.actSpeed = actSpeed;
    }


    public float getActSpeed() {
        return actSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public Position getPosition() {
        return position;
    }

    public double getChargingTimeInMinutes() {
        return chargingTimeInMinutes;
    }

    public double getConsumption() {
        return consumption;
    }

    public double getBatteryLoad() {
        return batteryLoad;
    }

    public String getId() {
        return id;
    }

    public void setActSpeed(float actSpeed) {
        this.actSpeed = actSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setChargingTimeInMinutes(double chargingTimeInMinutes) {
        this.chargingTimeInMinutes = chargingTimeInMinutes;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public void setBatteryLoad(double batteryLoad) {
        this.batteryLoad = batteryLoad;
    }

    public void setId(String id) {
        this.id = id;
    }
}
