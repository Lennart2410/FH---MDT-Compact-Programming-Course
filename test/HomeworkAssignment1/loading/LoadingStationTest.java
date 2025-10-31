package HomeworkAssignment1.loading;

import HomeworkAssignment1.general.Employee;
import HomeworkAssignment1.general.JobType;
import HomeworkAssignment1.loading.exceptions.DeliveryNotFoundException;
import HomeworkAssignment1.loading.exceptions.NoBayException;
import HomeworkAssignment1.loading.exceptions.NoDeliveryEmployeeException;
import HomeworkAssignment1.loading.vehicles.Truck;
import HomeworkAssignment1.loading.vehicles.Van;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoadingStationTest {

    static LoadingStation loadingStation;
    static Truck truck01;
    static Van van01;
    static Employee employee01;
    static Employee employee02;

    @BeforeAll
    static void setUp(){
        truck01 = new Truck(95.0,"Dortmund");
        van01 = new Van(33.0,"Berlin");
        employee01 = new Employee("Lennart Ziehm", 27, JobType.DELIVERY);
        employee02 = new Employee("Lennart Ziehm", 27, JobType.LOADER);
        loadingStation = new LoadingStation(1);
    }

    // Test 1 Dock Vehicle into Bay
    @Test
    public void dockVehicleIntoBayTest() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());
    }


    // Test 2 Dock Vehicle into Bay Exception
    @Test
    public void dockVehicleIntoBayTest_NoMoreBayException() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
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
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());

        Assertions.assertTrue(loadingStation.getEmployeeList().isEmpty());
        loadingStation.getEmployeeList().add(employee01);
        Assertions.assertEquals(1, loadingStation.getEmployeeList().size());

        String truck01Id = truck01.getId();
        loadingStation.startDeliveryById(truck01Id);
    }

    // Test 4 Start delivery by id deliverynotfound exception
    @Test
    public void startDeliveryById_DeliveryNotFoundException() throws NoBayException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());

        Assertions.assertTrue(loadingStation.getEmployeeList().isEmpty());
        loadingStation.getEmployeeList().add(employee01);
        Assertions.assertEquals(1, loadingStation.getEmployeeList().size());

        String truck01Id = "12345678";
        Assertions.assertThrows(DeliveryNotFoundException.class, ()->loadingStation.startDeliveryById(truck01Id));
    }

    // Test 5 Start delivery by id noemployee exception
    @Test
    public void startDeliveryById_NoDeliveryEmployeeException() throws NoBayException, DeliveryNotFoundException, NoDeliveryEmployeeException {
        Assertions.assertTrue(loadingStation.getDeliveryVehicles().isEmpty());
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        loadingStation.dockVehicleIntoBay(truck01);
        Assertions.assertEquals(1, loadingStation.getLoadingBayList().size());
        Assertions.assertEquals(1, loadingStation.getDeliveryVehicles().size());

        Assertions.assertTrue(loadingStation.getEmployeeList().isEmpty());
        loadingStation.getEmployeeList().add(employee02);
        Assertions.assertEquals(1, loadingStation.getEmployeeList().size());

        String truck01Id = truck01.getId();

        Assertions.assertThrows(NoDeliveryEmployeeException.class, ()->loadingStation.startDeliveryById(truck01Id));
    }
}
