package HomeworkAssignment3.loading;


import HomeworkAssignment3.general.*;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.loading.exceptions.*;
import HomeworkAssignment3.loading.vehicles.Car;
import HomeworkAssignment3.loading.vehicles.Truck;
import HomeworkAssignment3.loading.vehicles.Van;
import HomeworkAssignment3.packing.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadingStation extends Station<LoadingTask> {

    private List<Car> deliveryVehicles;
    private List<Employee> employeeList;
    private List<LoadingBay> loadingBayList;
    private final String warehouseUrl = "Warehouse";
    private final String loadingStationUrl = "LoadingStation";
    private final int maximumBayCapacity;
    // autoDelivery enables instant delivery instead of checking for space inside the vehicle
    private final boolean autoDelivery = true;

    private final ExecutorService deliveryExecutor = Executors.newCachedThreadPool();


    public LoadingStation(int maximumBayCapacity, BlockingQueue<Task> in, BlockingQueue<Task> out) throws NoBayException {
        super(in, out);
        deliveryVehicles = new ArrayList<>();
        employeeList = new ArrayList<>();
        loadingBayList = new ArrayList<>();
        this.maximumBayCapacity = maximumBayCapacity;

        for (int i = 0; i < maximumBayCapacity; i++) {
            loadingBayList.add(new LoadingBay());
        }
        employeeList.add(new Employee("Lennart Ziehm", 27, JobType.LOADER));
        employeeList.add(new Employee("Max Mustermann", 44, JobType.DELIVERY));

        // Dock a vehicle into the docking-bay
        dockVehicleIntoBay(new Van(30.0, "Dortmund"));
        dockVehicleIntoBay(new Truck(95.0, "Dortmund"));
    }

    @Override
    public void process(LoadingTask loadingTask) throws WarehouseException {


        //Retrieve Parcels from order
        Order currentOrder = loadingTask.getOrder();
        List<Parcel> parcelList = currentOrder.getOrderParcels();

        try {

            logManager.writeLogEntry("A LoadingTask with the order number " + loadingTask.getOrder().getOrderNumber() + " should be processed.", warehouseUrl);


            if (parcelList.isEmpty()) {
                logManager.writeLogEntry("ERROR: Parcellist in order was emtpy.", loadingStationUrl);
                throw new NoParcelException("There were no parcels inside the order.");
            }

            List<LoadingBay> loadingBaysWithMatchingDestination = searchLoadingBayByDestination(currentOrder.getDestination());
            if (loadingBaysWithMatchingDestination.isEmpty()) {
                logManager.writeLogEntry("ERROR: There were no delivery vehicles with a matching destination.", loadingStationUrl);
                throw new NoDestinationException("There was no matching car for the selected destination.");
            }

            String carIdToStart = loadVehicle(loadingBaysWithMatchingDestination, parcelList);
            if (carIdToStart.isBlank()) {
                throw new LoadingException("There was no id found for loading the delivery vehicle.");
            }
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADED);

            System.out.println("LoadingStation finished loading. Parcel is ready for delivery.");
            logManager.writeLogEntry("A LoadingTask with the order number " + loadingTask.getOrder().getOrderNumber() + " was processed and is ready for delivery.", warehouseUrl);

            if (isDeliveryReadyToStart(carIdToStart)) {
                System.out.println("A delivery for Id " + carIdToStart + " is going to start.");
                logManager.writeLogEntry("A delivery for Id " + carIdToStart + " is going to start.", loadingStationUrl);
                startDeliveryById(carIdToStart, currentOrder);
            } else {
                logManager.writeLogEntry("Delivery vehicle with Id " + carIdToStart + " is not starting, because there is still room left for more packages.", loadingStationUrl);
            }


        } catch (NoMoreSpaceException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADING);
            logManager.writeLogEntry("WARN: " + e.getMessage(), loadingStationUrl);
        } catch (NoDeliveryEmployeeException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADED);
            logManager.writeLogEntry("WARN: No delivery driver found. Reschedule for later.", loadingStationUrl);
        } catch (NoBayException | NoLoadingEmployeeException e) {
            currentOrder.setOrderStatusEnum(OrderStatusEnum.LOADING);
            logManager.writeLogEntry("WARN: Please try rescheduling the loading and delivery of the parcel again later.", loadingStationUrl);
        } catch (CarNotFoundException e) {
            logManager.writeLogEntry("ERROR: " + e.getMessage(), loadingStationUrl);
            throw new WarehouseException("A generic error inside the warehouse occured.");
        } catch (NoDestinationException e) {
            logManager.writeLogEntry("WARN: " + e.getMessage() + " Put the Task into the queue once more.", loadingStationUrl);
            loadingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.PACKAGING);
            this.in.add(loadingTask);
        } catch (LoadingException | DeliveryException e) {
            logManager.writeLogEntry("ERROR: " + e.getMessage(), loadingStationUrl);
            currentOrder.setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
            throw new WarehouseException("A Generic error inside the warehouse occured.");
        }
        logManager.writeLogEntry("Loading station task is complete.", warehouseUrl);
        // No putting into queues here, because order is finished
    }


    public void dockVehicleIntoBay(Car car) throws NoBayException {
        // Search for free bay and dock car into it
        logManager.writeLogEntry("Trying to dock delivery vehicle: " + car.getId(), loadingStationUrl);
        LoadingBay freeLoadingBay = loadingBayList.stream().filter(loadingBay -> loadingBay.getOccupyingCar() == null).findFirst().orElseThrow(() -> new NoBayException("There was no bay left to dock another delivery vehicle."));
        freeLoadingBay.setOccupyingCar(car);
        if (!deliveryVehicles.contains(car)) {
            deliveryVehicles.add(car);
        }
        car.setCurrentlyDelivering(false);
        logManager.writeLogEntry("Docking the delivery vehicle " + car.getId() + " was successfully.", loadingStationUrl);
    }

    public void undockVehicleFromBay(Car car) {
        loadingBayList.removeIf(loadingBay -> loadingBay.getOccupyingCar().getId().equals(car.getId()));
        car.setCurrentlyDelivering(true);
    }

    public String loadVehicle(List<LoadingBay> loadingBaysWithMatchingDestination, List<Parcel> parcelList) throws NoLoadingEmployeeException, NoMoreSpaceException {

        String carIdToStart = "";
        List<Parcel> addedParcels = new ArrayList<>();
        for (LoadingBay loadingBay : loadingBaysWithMatchingDestination) {
            for (Parcel parcel : parcelList) {
                logManager.writeLogEntry("Parcel " + parcel.getId() + " is trying to be loaded.", loadingStationUrl);
                if (loadingBay.getOccupyingCar().getCurrentCapacity() > parcel.getWeightKg()) {
                    carIdToStart = loadingBay.getOccupyingCar().getId();

                    Employee loadingEmployee = employeeList.stream().filter(employee -> employee.getJobType().equals(JobType.LOADER)).findFirst().orElseThrow(() -> new NoLoadingEmployeeException("There were no free employees who would stash in the parcel."));


                    loadingEmployee.setCurrentlyOccupied(true);
                    loadingBay.getOccupyingCar().addParcel(parcel);
                    addedParcels.add(parcel);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    logManager.writeLogEntry("Parcel " + parcel.getId() + " was loaded.", loadingStationUrl);
                    loadingEmployee.setCurrentlyOccupied(false);

                } else {
                    logManager.writeLogEntry("Parcel: " + parcel.getId() + " did not fit into delivery vehicle: " + loadingBay.getOccupyingCar().getId() + ". Looking for another delivery vehicle.", loadingStationUrl);
                }
            }
            //If Parcels are all loaded, stop outer loop
            if(addedParcels.size() == parcelList.size()){
                break;
            }
        }

        if (addedParcels.size() != parcelList.size() && parcelList.size() > 1) {
            logManager.writeLogEntry("ERROR: Not all parcels fit into delivery vehicle. Order will be split", loadingStationUrl);
            throw new NoMoreSpaceException("Not all Parcels could be loaded into delivery vehicles.");
        }
        return carIdToStart;
    }

    public void startDeliveryById(String id, Order currentOrder) throws NoDeliveryEmployeeException, DeliveryNotFoundException, NoBayException {
        logManager.writeLogEntry("Looking to start delivery with Id: " + id, loadingStationUrl);
        Car deliveryVehicleToStart = deliveryVehicles.stream().filter(deliveryVehicle -> deliveryVehicle.getId().equals(id)).findFirst().orElseThrow(() -> new DeliveryNotFoundException("A delivery with the given id could not be found."));

        Employee deliveryEmployee = employeeList.stream().filter(employee -> employee.getJobType().equals(JobType.DELIVERY)).findFirst().orElseThrow(() -> new NoDeliveryEmployeeException("There were no free employees who would could drive the delivery."));

        deliveryExecutor.submit(() -> {
            undockVehicleFromBay(deliveryVehicleToStart);
            deliveryEmployee.setCurrentlyOccupied(true);
            logManager.writeLogEntry("Starting delivery with Id " + id + ".", loadingStationUrl);
            deliveryVehicleToStart.drive();

            deliveryEmployee.setCurrentlyOccupied(false);
            logManager.writeLogEntry("Completed the delivery for " + id + ".", loadingStationUrl);
            currentOrder.setOrderStatusEnum(OrderStatusEnum.DELIVERED);
            System.out.println("A delivery for Id " + id + " is finished.");
            try {
                dockVehicleIntoBay(deliveryVehicleToStart);
            } catch (NoBayException e) {
                currentOrder.setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
                throw new RuntimeException(e);
            }
            logManager.writeLogEntry("Delivery with id: " + id + " was successfully delivered.", warehouseUrl);
        });
    }


    public List<LoadingBay> searchLoadingBayByDestination(String destination) {
        logManager.writeLogEntry("Searching for a bay with the destination of: " + destination, loadingStationUrl);
        // Search for a loading bay with the right destination
        return loadingBayList.stream().filter(loadingBay -> loadingBay.getOccupyingCar().getDesination().equals(destination)).toList();
    }

    private boolean isDeliveryReadyToStart(String carIdToStart) throws CarNotFoundException {
        double currentCapacity = loadingBayList.stream().filter(loadingBay -> loadingBay.getOccupyingCar().getId().equals(carIdToStart)).findFirst().orElseThrow(() -> new CarNotFoundException("The Car with the id " + carIdToStart + " could not be found.")).getOccupyingCar().getCurrentCapacity();
        return autoDelivery ? autoDelivery : currentCapacity < 30.0;
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
