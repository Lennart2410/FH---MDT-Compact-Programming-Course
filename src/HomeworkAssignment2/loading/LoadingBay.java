package HomeworkAssignment2.loading;

import HomeworkAssignment2.loading.vehicles.Car;

public class LoadingBay {
    private String bayNumber;
    private Car occupyingCar;

    private static int globalBayNumber = 0;

    public LoadingBay(){
        this.bayNumber = "BAY-" + globalBayNumber++;
        this.occupyingCar = null;
    }

    public String getBayNumber() {
        return bayNumber;
    }

    public Car getOccupyingCar() {
        return occupyingCar;
    }

    public void setOccupyingCar(Car occupyingCar) {
        this.occupyingCar = occupyingCar;
    }
}
