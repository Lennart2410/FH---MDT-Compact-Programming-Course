package HomeworkAssignment1.picking;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.picking.PickingTask;

import java.util.List;

public class PickingStationTest {

    @Test
    public void testSuccessfulPick() {
        System.out.println("✅ This test ran successfully");

        Order order = new Order("Test Street", List.of(new Item("Book")));
        PickingTask task = new PickingTask(order, "Shelf-A1", "Yasaman", true);
        PickingStation station = new PickingStation();

        Order result = station.process(task);
        assertEquals(order.toString(), result.toString());
    }

    @Test
    public void testItemUnavailableThrowsException() {
        Order order = new Order("Test Street", List.of(new Item("Book")));
        PickingTask task = new PickingTask(order, "Shelf-A1", "Yasaman", false);
        PickingStation station = new PickingStation();

        assertThrows(PickingException.class, () -> station.process(task));
    }

    @Test
    public void testShelfLocationIsStoredCorrectly() {
        PickingTask task = new PickingTask(new Order("Test", List.of()), "Shelf-B2", "Yasaman", true);
        assertEquals("Shelf-B2", task.getShelfLocation());
    }

    @Test
    public void testPickerNameIsStoredCorrectly() {
        PickingTask task = new PickingTask(new Order("Test", List.of()), "Shelf-B2", "Yasaman", true);
        assertEquals("Yasaman", task.getPickerName());
    }

    @Test
    public void testItemAvailabilityFlag() {
        PickingTask task = new PickingTask(new Order("Test", List.of()), "Shelf-B2", "Yasaman", false);
        assertFalse(task.isItemAvailable());
    }
}