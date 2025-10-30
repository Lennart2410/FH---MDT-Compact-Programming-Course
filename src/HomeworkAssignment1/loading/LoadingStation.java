package HomeworkAssignment1.loading;

import HomeworkAssignment1.general.*;
import HomeworkAssignment1.loading.vehicles.Car;
import HomeworkAssignment1.loading.vehicles.Truck;
import HomeworkAssignment1.loading.vehicles.Van;
import HomeworkAssignment1.logging.LogFiles;
import HomeworkAssignment1.packing.Parcel;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoadingStation extends Station<LoadingTask> {

    private List<Car> deliveryVehicles;
    private List<Employee> employeeList;
    private List<LoadingBay> loadingBayList;
    private final String baseUrl = System.getProperty("user.dir") + "/logs/LoadingStation";
    private final LogFiles logManager = new LogFiles(Paths.get(baseUrl));
    private final int maximumBayCapacity;


    public LoadingStation(int maximumBayCapacity) {
        deliveryVehicles = new ArrayList<>();
        employeeList = List.of(new Employee("Lennart Ziehm", 27, JobType.LOADER), new Employee("Max Mustermann", 44, JobType.DELIVERY));
        loadingBayList = List.of(new LoadingBay(), new LoadingBay());
        this.maximumBayCapacity = maximumBayCapacity;

        // Dock a vehicle into the docking-bay
        dockVehicleIntoBay(new Van(30.0, "Dortmund"));
        dockVehicleIntoBay(new Truck(95.0, "Dortmund"));
    }

    @Override
    public Order process(LoadingTask loadingTask) {
        try {
            writeLogEntry("");

            //Retrieve Parcels from order
            Order currentOrder = loadingTask.getOrder();
            List<Parcel> parcelList = currentOrder.getOrderParcels();
            if (parcelList.isEmpty()) {
                //Exception
            }

            List<LoadingBay> loadingBaysWithMatchingDestination = searchLoadingBayByDestination("Dortmund");
            if (loadingBaysWithMatchingDestination.isEmpty()) {
                // Exception
            }

            loadVehicle(loadingBaysWithMatchingDestination, parcelList);

            startDeliveryById("Car00001");

            return loadingTask.getOrder();
        } catch (Exception e) {
            loadingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
        }

        return loadingTask.getOrder();
    }


    private void dockVehicleIntoBay(Car car) {
        // Search for free bay and dock car into it
        if(!(maximumBayCapacity > loadingBayList.size())){
            // ToDo: Exception einbauen
        }

    }

    private void loadVehicle(List<LoadingBay> loadingBaysWithMatchingDestination, List<Parcel> parcelList) {
        List<Parcel> addedParcels = new ArrayList<>();
        for (LoadingBay loadingBay : loadingBaysWithMatchingDestination) {
            for (Parcel parcel : parcelList) {
                if (loadingBay.getOccupyingCar().getCurrentCapacity() > parcel.getWeightKg()) {
                    loadingBay.getOccupyingCar().addParcel(parcel);
                    addedParcels.add(parcel);
                }
            }
        }

        if (addedParcels.size() != parcelList.size()) {
            // ToDo: Exception einbauen
        }
    }

    private void startDeliveryById(String id) {
        // ToDo: Exception einbauen
        Car deliveryVehicleToStart = deliveryVehicles.stream().filter(deliveryVehicle -> deliveryVehicle.getId().equals(id)).findFirst().orElseThrow(RuntimeException::new);
        deliveryVehicleToStart.drive();
    }


    private List<LoadingBay> searchLoadingBayByDestination(String destination) {
        List<LoadingBay> loadingBayWithCorrectDestinationList = loadingBayList.stream().filter(loadingBay -> loadingBay.getOccupyingCar().getDesination().equals(destination)).toList();
        // Search for a loading bay with the right destination
        return loadingBayWithCorrectDestinationList;
    }


    private void writeLogEntry(String textToLog) {
        try {
            logManager.appendLine(Paths.get(baseUrl + "test.txt"), "");
        } catch (IOException e) {
            // Log not possible
        }
    }
}
