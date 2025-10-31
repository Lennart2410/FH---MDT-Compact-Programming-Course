package HomeworkAssignment2.loading;

import HomeworkAssignment2.packing.Parcel;
import HomeworkAssignment2.general.*;
import HomeworkAssignment2.general.exceptions.WarehouseException;
import HomeworkAssignment2.loading.exceptions.*;
import HomeworkAssignment2.loading.vehicles.Car;
import HomeworkAssignment2.loading.vehicles.Truck;
import HomeworkAssignment2.loading.vehicles.Van;
import HomeworkAssignment2.logging.LogFiles;

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
        employeeList = new ArrayList<>();
        loadingBayList = new ArrayList<>();
        this.maximumBayCapacity = maximumBayCapacity;

        for (int i = 0; i < maximumBayCapacity; i++) {
            loadingBayList.add(new LoadingBay());
        }
    }

    @Override
    public Order process(LoadingTask loadingTask) throws WarehouseException {


        //Retrieve Parcels from order
        Order currentOrder = loadingTask.getOrder();
        List<Parcel> parcelList = currentOrder.getOrderParcels();

        try {
            employeeList.add(new Employee("Lennart Ziehm", 27, JobType.LOADER));
            employeeList.add(new Employee("Max Mustermann", 44, JobType.DELIVERY));

            // Dock a vehicle into the docking-bay
            dockVehicleIntoBay(new Van(30.0, "Dortmund"));
            dockVehicleIntoBay(new Truck(95.0, "Dortmund"));


            writeLogEntry("A LoadingTask with the order number " + loadingTask.getOrder().getOrderNumber() + " should be processed.", "warehouse");


            if (parcelList.isEmpty()) {
                writeLogEntry("ERROR: Parcellist in order was emtpy.");
                throw new NoParcelException("There were no parcels inside the order.");
            }

            List<LoadingBay> loadingBaysWithMatchingDestination = searchLoadingBayByDestination("Dortmund");
            if (loadingBaysWithMatchingDestination.isEmpty()) {
                writeLogEntry("ERROR: There were no delivery vehicles with a matching destination.");
                throw new NoDestinationException("There was no matching destination for this ");
            }

            loadVehicle(loadingBaysWithMatchingDestination, parcelList);
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADED);

            startDeliveryById("Car00001");
            currentOrder.setOrderStatusEnum(OrderStatusEnum.DELIVERED);
            writeLogEntry("A LoadingTask with the order number " + loadingTask.getOrder().getOrderNumber() + " was processed.", "warehouse");

            return loadingTask.getOrder();
        } catch (NoMoreSpaceException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADING);
            writeLogEntry("WARN: " + e.getMessage());
        } catch (NoDeliveryEmployeeException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADED);
            writeLogEntry("WARN: No delivery driver found. Reschedule for later.");
        } catch (NoBayException | NoLoadingEmployeeException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADING);
            writeLogEntry("WARN: Please try rescheduling the loading and delivery of the parcel again later.");
        } catch (LoadingException | DeliveryException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
            throw new WarehouseException("a Generic error inside the warehosue occured.");
        }
        // Returning updated Order with exception state
        return currentOrder;
    }


    public void dockVehicleIntoBay(Car car) throws NoBayException {
        // Search for free bay and dock car into it
        writeLogEntry("Trying to dock delivery vehicle: " + car.getId());
        LoadingBay freeLoadingBay = loadingBayList.stream().filter(loadingBay -> loadingBay.getOccupyingCar() == null).findFirst().orElseThrow(() -> new NoBayException("There was no bay left to dock another delivery vehicle."));
        freeLoadingBay.setOccupyingCar(car);
        deliveryVehicles.add(car);
        writeLogEntry("Docking the delivery vehicle " + car.getId() + " was not possible, because all bays are occupied.");

    }

    public void loadVehicle(List<LoadingBay> loadingBaysWithMatchingDestination, List<Parcel> parcelList) throws NoLoadingEmployeeException, NoMoreSpaceException {
        List<Parcel> addedParcels = new ArrayList<>();
        for (LoadingBay loadingBay : loadingBaysWithMatchingDestination) {
            for (Parcel parcel : parcelList) {
                writeLogEntry("Parcel " + parcel.getId() + " is trying to be loaded.");
                if (loadingBay.getOccupyingCar().getCurrentCapacity() > parcel.getWeightKg()) {
                    Employee loadingEmployee = employeeList.stream().filter(employee -> employee.getJobType().equals(JobType.LOADER)).findFirst().orElseThrow(() -> new NoLoadingEmployeeException("There were no free employees who would stash in the parcel."));

                    loadingEmployee.setCurrentlyOccupied(true);
                    loadingBay.getOccupyingCar().addParcel(parcel);
                    addedParcels.add(parcel);
                    writeLogEntry("Parcel " + parcel.getId() + " was loaded.");
                    loadingEmployee.setCurrentlyOccupied(false);
                }
                writeLogEntry("Parcel: " + parcel.getId() + " did not fit into delivery vehicle: " + loadingBay.getOccupyingCar().getId() + ". Looking for another delivery vehicle.");
            }
        }

        if (addedParcels.size() != parcelList.size()) {
            writeLogEntry("ERROR: Not all parcels fit into delivery vehicle. Order will be split");
            throw new NoMoreSpaceException("Not all Parcels could be loaded into delivery vehicles.");
        }
    }

    public void startDeliveryById(String id) throws NoDeliveryEmployeeException, DeliveryNotFoundException {
        writeLogEntry("Looking to start delivery with Id: " + id);
        Car deliveryVehicleToStart = deliveryVehicles.stream().filter(deliveryVehicle -> deliveryVehicle.getId().equals(id)).findFirst().orElseThrow(() -> new DeliveryNotFoundException("A delivery with the given id could not be found."));
        Employee deliveryEmployee = employeeList.stream().filter(employee -> employee.getJobType().equals(JobType.DELIVERY)).findFirst().orElseThrow(() -> new NoDeliveryEmployeeException("There were no free employees who would could drive the delivery."));
        deliveryEmployee.setCurrentlyOccupied(true);
        writeLogEntry("Starting delivery with Id " + id + ".");
        deliveryVehicleToStart.drive();
        writeLogEntry("Delivery with Id " + id + " was successful.");
        deliveryEmployee.setCurrentlyOccupied(false);
    }


    public List<LoadingBay> searchLoadingBayByDestination(String destination) {
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


    public List<Car> getDeliveryVehicles() {
        return deliveryVehicles;
    }

    public void setDeliveryVehicles(List<Car> deliveryVehicles) {
        this.deliveryVehicles = deliveryVehicles;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<LoadingBay> getLoadingBayList() {
        return loadingBayList;
    }

    public void setLoadingBayList(List<LoadingBay> loadingBayList) {
        this.loadingBayList = loadingBayList;
    }

    public int getMaximumBayCapacity() {
        return maximumBayCapacity;
    }
}
