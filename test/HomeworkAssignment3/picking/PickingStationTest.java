package HomeworkAssignment3.picking;


import HomeworkAssignment3.general.Item;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.OrderStatusEnum;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.picking.exceptions.PickingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class PickingStationTest {

    PickingStation station;

    @BeforeEach
    public void setUp() {
        BlockingQueue<Task> ingoingQueue = new ArrayBlockingQueue<>(1);
        BlockingQueue<Task> outgoingQueue =  new ArrayBlockingQueue<>(1);
        station = new PickingStation(ingoingQueue,outgoingQueue);
    }

    @Test
    public void testSuccessfulPick() throws WarehouseException {
        System.out.println(" This test ran successfully");

        Order order = new Order("Test Street", List.of(new Item("Book")));
        PickingTask task = new PickingTask(order, "Shelf-A1", true);


        assertEquals(order.getOrderStatusEnum(), OrderStatusEnum.ORDERED);
        station.process(task);
        assertEquals(order.getOrderStatusEnum(), OrderStatusEnum.PICKED);
    }

    @Test
    public void testItemUnavailableThrowsException() {
        Order order = new Order("Test Street", List.of(new Item("Book")));
        PickingTask task = new PickingTask(order, "Shelf-A1", false);


        assertThrows(PickingException.class, () -> station.process(task));
    }

    @Test
    public void testShelfLocationIsStoredCorrectly() {
        PickingTask task = new PickingTask(new Order("Test", List.of()), "Shelf-B2", true);
        assertEquals("Shelf-B2", task.getShelfLocation());
    }

    @Test
    public void testPickerNameIsStoredCorrectly() {
        PickingTask task = new PickingTask(new Order("Test", List.of()), "Shelf-B2", true);
        //assertEquals("Yasaman", task.getPickerName());
    }

    @Test
    public void testItemAvailabilityFlag() {
        PickingTask task = new PickingTask(new Order("Test", List.of()), "Shelf-B2", false);
        assertFalse(task.isItemAvailable());
    }
}