package SelfAssignment3;

import java.time.LocalDate;

public class AGV extends HardwareResource {
    private double batteryLoad;
    private double consumption;
    private double chargingTimeInMinutes;

    private float maxSpeed;
    private float actSpeed;

    public AGV(String id, double batteryLoad, double consumption, Position position, double chargingTimeInMinutes,
               float maxSpeed, float actSpeed, LocalDate purchaseDate) {

        super(id, position, purchaseDate);

        this.batteryLoad = batteryLoad;
        this.consumption = consumption;
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


    public double getChargingTimeInMinutes() {
        return chargingTimeInMinutes;
    }

    public double getConsumption() {
        return consumption;
    }

    public double getBatteryLoad() {
        return batteryLoad;
    }


    public void setActSpeed(float actSpeed) {
        this.actSpeed = actSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
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


    public String toString() {
        return super.toString() +
                "Battery load: " + getBatteryLoad() + "\n" +
                "Consumption rate: " + getConsumption() + "\n" +
                "Charging duration: " + getChargingTimeInMinutes() + "min" + "\n" +
                "Maximum speed: " + getMaxSpeed() + "\n" +
                "Standard action speed: " + getActSpeed() + "\n";
    }
}