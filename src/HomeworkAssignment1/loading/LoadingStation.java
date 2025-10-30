package HomeworkAssignment1.loading;

import HomeworkAssignment1.general.*;
import HomeworkAssignment1.loading.vehicles.Car;
import HomeworkAssignment1.loading.vehicles.Truck;
import HomeworkAssignment1.loading.vehicles.Van;
import HomeworkAssignment1.logging.LogFiles;
import HomeworkAssignment1.packing.Parcel;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LoadingStation extends Station<LoadingTask> {

    private List<Car> deliveryVehicles;
    private List<Employee> employeeList;
    private List<LoadingBay> loadingBayList;
    private final String baseUrl = "logs";
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
        //Retrieve Parcels from order
        Order currentOrder = loadingTask.getOrder();
        List<Parcel> parcelList = currentOrder.getOrderParcels();

        try {
            writeLogEntry("A LoadingTask with the order number " + loadingTask.getOrder().getOrderNumber() + " should be processed.", "warehouse");


            if (parcelList.isEmpty()) {
                writeLogEntry("ERROR: Parcellist in order was emtpy.");
                // ToDo: throw new Exception -> new NoParcelException();
            }

            List<LoadingBay> loadingBaysWithMatchingDestination = searchLoadingBayByDestination("Dortmund");
            if (loadingBaysWithMatchingDestination.isEmpty()) {
                writeLogEntry("ERROR: There were no delivery vehicles with a matching destination.");
                // ToDo: throw new Exception -> new NoDestinationExeption();
            }

            loadVehicle(loadingBaysWithMatchingDestination, parcelList);
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADED);

            startDeliveryById("Car00001");
            currentOrder.setOrderStatusEnum(OrderStatusEnum.DELIVERED);
            writeLogEntry("A LoadingTask with the order number " + loadingTask.getOrder().getOrderNumber() + " was processed.", "warehouse");

            return loadingTask.getOrder();
        } catch (Exception e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
        }
        // Returning updated Order with exception state
        return currentOrder;
    }


    private void dockVehicleIntoBay(Car car) {
        // Search for free bay and dock car into it
        writeLogEntry("Trying to dock delivery vehicle: " + car.getId());
        if (!(maximumBayCapacity > loadingBayList.size())) {
            writeLogEntry("Docking the delivery vehicle " + car.getId() + " was not possible, because all bays are occupied.");
            // ToDo: throw new Exception -> new NoMoreBayException();
        }

    }

    private void loadVehicle(List<LoadingBay> loadingBaysWithMatchingDestination, List<Parcel> parcelList) {
        List<Parcel> addedParcels = new ArrayList<>();
        for (LoadingBay loadingBay : loadingBaysWithMatchingDestination) {
            for (Parcel parcel : parcelList) {
                writeLogEntry("Parcel " + parcel.getId() + " is trying to be loaded.");
                if (loadingBay.getOccupyingCar().getCurrentCapacity() > parcel.getWeightKg()) {
                    Employee loadingEmployee = employeeList.stream().filter(employee -> employee.getJobType().equals(JobType.LOADER)).findFirst().orElseThrow(RuntimeException::new);
                    // ToDo: Exception einbauen -> new NoEmployeeException();
                    loadingEmployee.setCurrentlyOccupied(true);
                    loadingBay.getOccupyingCar().addParcel(parcel);
                    addedParcels.add(parcel);
                    writeLogEntry("Parcel " + parcel.getId() + " was loaded.");
                    loadingEmployee.setCurrentlyOccupied(false);
                }
                writeLogEntry("Parcel: " + parcel.getId() + " did not fit into delivery vehicle: " + loadingBay.getOccupyingCar().getId()+ ". Looking for another delivery vehicle.");
            }
        }

        if (addedParcels.size() != parcelList.size()) {
            // ToDo: Exception einbauen -> new NoMoreSpaceException();
            writeLogEntry("ERROR: Not all parcels fit into delivery vehicle. Order will be split");
        }
    }

    private void startDeliveryById(String id) {
        writeLogEntry("Looking to start delivery with Id: " + id);
        // ToDo: Exception einbauen -> new DeliveryNotFoundException();
        Car deliveryVehicleToStart = deliveryVehicles.stream().filter(deliveryVehicle -> deliveryVehicle.getId().equals(id)).findFirst().orElseThrow(RuntimeException::new);
        // ToDo: Exception einbauen -> new NoEmployeeException();
        Employee deliveryEmployee = employeeList.stream().filter(employee -> employee.getJobType().equals(JobType.DELIVERY)).findFirst().orElseThrow(RuntimeException::new);
        deliveryEmployee.setCurrentlyOccupied(true);
        writeLogEntry("Starting delivery with Id " + id+".");
        deliveryVehicleToStart.drive();
        writeLogEntry("Delivery with Id " + id + " was successful.");
        deliveryEmployee.setCurrentlyOccupied(false);
    }


    private List<LoadingBay> searchLoadingBayByDestination(String destination) {
        writeLogEntry("Searching for a bay with the destination of: " + destination);
        // Search for a loading bay with the right destination
        return loadingBayList.stream().filter(loadingBay -> loadingBay.getOccupyingCar().getDesination().equals(destination)).toList();
    }


    private void writeLogEntry(String textToLog) {
        try {
            Path pathToWrite = logManager.pathFor("loading", "LoadingStation", LocalDate.now());
            logManager.appendLine(pathToWrite, LocalTime.now() + ": " + textToLog);
        } catch (IOException e) {
            // Log not possible
        }
    }

    private void writeLogEntry(String textToLog, String destination) {
        try {
            Path pathToWrite = logManager.pathFor(destination, null, LocalDate.now());
            logManager.appendLine(pathToWrite, LocalTime.now() + ": " + textToLog);
        } catch (IOException e) {
            // Log not possible
        }
    }
}
