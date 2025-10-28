package HomeworkAssignment1.loading;

import HomeworkAssignment1.loading.vehicles.Car;

public class LoadingBay {
    private String bayNumber;
    private Car occupyingCar;

    private static int globalBayNumber = 0;

    public LoadingBay(){
        this.bayNumber = "BAY-" + globalBayNumber++;
    }

    public String getBayNumber() {
        return bayNumber;
    }

    public Car getOccupyingCar() {
        return occupyingCar;
    }


}
