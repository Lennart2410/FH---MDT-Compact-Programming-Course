package HomeworkAssignment3.loading;


import HomeworkAssignment3.general.Employee;
import HomeworkAssignment3.general.JobType;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.loading.exceptions.DeliveryNotFoundException;
import HomeworkAssignment3.loading.exceptions.NoBayException;
import HomeworkAssignment3.loading.exceptions.NoDeliveryEmployeeException;
import HomeworkAssignment3.loading.vehicles.Truck;
import HomeworkAssignment3.loading.vehicles.Van;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LoadingStationTest {

    static LoadingStation loadingStation;
    static Truck truck01;
    static Van van01;
    static Employee employee01;
    static Employee employee02;
    BlockingQueue<Task> ingoingQueue;
    BlockingQueue<Task> outgoingQueue;

    @BeforeEach
    void setUp() throws NoBayException {
        ingoingQueue = new ArrayBlockingQueue<>(1);
        outgoingQueue = new ArrayBlockingQueue<>(1);

        truck01 = new Truck(95.0,"Dortmund");
        van01 = new Van(33.0,"Berlin");
        employee01 = new Employee("Lennart Ziehm", 27, JobType.DELIVERY);
        employee02 = new Employee("Lennart Ziehm", 27, JobType.LOADER);
        loadingStation = new LoadingStation(2, ingoingQueue, outgoingQueue, null);
        loadingStation.getLoadingBayList().clear();
        loadingStation.getDeliveryVehicles().clear();
        loadingStation.getEmployeeList().clear();
    }

    // Test 1 Dock Vehicle into Bay
    @Test
    public void dockVehicleIntoBayTest() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        loadingStation.getLoadingBayList().add(new LoadingBay());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());
    }


    // Test 2 Dock Vehicle into Bay Exception
    @Test
    public void dockVehicleIntoBayTest_NoMoreBayException() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        loadingStation.getLoadingBayList().add(new LoadingBay());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());
        Assertions.assertThrows(NoBayException.class, () ->loadingStation.dockVehicleIntoBay(van01));
    }

    // Test 3 Start delivery by id
    @Test
    public void startDeliveryById() throws NoBayException, DeliveryNotFoundException, NoDeliveryEmployeeException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        loadingStation.getLoadingBayList().add(new LoadingBay());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());

        Assertions.assertTrue(loadingStation.getEmployeeList().isEmpty());
        loadingStation.getEmployeeList().add(employee01);
        Assertions.assertEquals(1, loadingStation.getEmployeeList().size());

        String truck01Id = truck01.getId();
        loadingStation.startDeliveryById(truck01Id,null);
    }

    // Test 4 Start delivery by id deliverynotfound exception
    @Test
    public void startDeliveryById_DeliveryNotFoundException() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        loadingStation.getLoadingBayList().add(new LoadingBay());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());

        Assertions.assertTrue(loadingStation.getEmployeeList().isEmpty());
        loadingStation.getEmployeeList().add(employee01);
        Assertions.assertEquals(1, loadingStation.getEmployeeList().size());

        String truck01Id = "12345678";
        Assertions.assertThrows(DeliveryNotFoundException.class, ()->loadingStation.startDeliveryById(truck01Id,null));
    }

    // Test 5 Start delivery by id noemployee exception
    @Test
    public void startDeliveryById_NoDeliveryEmployeeException() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        loadingStation.getLoadingBayList().add(new LoadingBay());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());

        Assertions.assertTrue(loadingStation.getEmployeeList().isEmpty());
        loadingStation.getEmployeeList().add(employee02);
        Assertions.assertEquals(1, loadingStation.getEmployeeList().size());

        String truck01Id = truck01.getId();

        Assertions.assertThrows(NoDeliveryEmployeeException.class, ()->loadingStation.startDeliveryById(truck01Id,null));
    }
}
