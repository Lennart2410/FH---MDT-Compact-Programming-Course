package HomeworkAssignment1.loading;

import HomeworkAssignment1.general.Employee;
import HomeworkAssignment1.general.JobType;
import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Station;
import HomeworkAssignment1.loading.vehicles.Car;
import HomeworkAssignment1.loading.vehicles.Truck;
import HomeworkAssignment1.loading.vehicles.Van;

import java.util.ArrayList;
import java.util.List;

public class LoadingStation extends Station<LoadingTask> {

    List<Car> deliveryVehicles;
    List<Employee> employeeList;
    List<LoadingBay> loadingBayList;

    public LoadingStation(){
        deliveryVehicles = List.of(new Van(), new Truck());
        employeeList = List.of(new Employee("Lennart Ziehm",27, JobType.LOADER),new Employee("Max Mustermann",44, JobType.DELIVERY) );
        loadingBayList = List.of(new LoadingBay(), new LoadingBay());
    }

    @Override
    public Order process(LoadingTask loadingTask) {
        //6. Access Order.txt and update things like OrderStatus -> LOADED / LOADING

        // Loading items from the order
        // Do something that the Loading-process would usually cover
        // Process should take some seconds to make it realistic


        Order order = loadingTask.getOrder();
        System.out.println("Processing Order with order number " + order.getOrderNumber());

        Truck truck = new Truck();

        dockVehicleIntoBay(truck);

        List<LoadingBay> loadingBaysWithMatchingDestination = searchLoadingBayByDestination("Dortmund");

        loadVehicle(loadingBaysWithMatchingDestination.get(0),loadingTask.getOrder());
        // load vehicles of this class with order


        startDeliveryById("123456");

        System.out.println(order.getOrderNumber() + " is being processed at the loading station."); // -> Write to LoadingStation.txt

        System.out.println("Im im loading!");

        return loadingTask.getOrder();
    }


    private void dockVehicleIntoBay(Car car){
        // Search for free bay and dock car into it
    }

    private void loadVehicle(LoadingBay loadingBay, Order order){
        // Check

        // access classes vehicle list
        // access classes employee list

        // check if parcel fits into vehicle
        // check if parcel is too heavy for the vehicle
        // check if enough employees are present for loading -> at threshold 2 employees are needed
    }

    private void startDeliveryById(String id){
        // check if enough employees are present to drive -> needs to have right role
        //search for vehicle by id
        Truck deliveryVehicle = new Truck();
        deliveryVehicle.drive();
    }


    private List<LoadingBay> searchLoadingBayByDestination(String destination){
        List<LoadingBay> loadingBayWithCorrectDestinationList = new ArrayList<>();
        // Search for a loading bay with the right destination
        return loadingBayWithCorrectDestinationList;
    }
}
