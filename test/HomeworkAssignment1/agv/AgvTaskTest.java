package HomeworkAssignment1.agv;

import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgvTaskTest {

    Order sampleOrder = new Order("Test Street", List.of(new Item("Phone")));

    @Test
    public void testGetOrder() {
        AgvTask task = new AgvTask(sampleOrder, "A", "B", "AGV-01");
        assertEquals(sampleOrder, task.getOrder());
    }

    @Test
    public void testGetStartingLocation() {
        AgvTask task = new AgvTask(sampleOrder, "Start", "End", "AGV-01");
        assertEquals("Start", task.getStartingLocation());
    }

    @Test
    public void testGetDestinationLocation() {
        AgvTask task = new AgvTask(sampleOrder, "Start", "End", "AGV-01");
        assertEquals("End", task.getDestinationLocation());
    }

    @Test
    public void testGetAgvId() {
        AgvTask task = new AgvTask(sampleOrder, "Start", "End", "AGV-99");
        assertEquals("AGV-99", task.getAgvId());
    }

    @Test
    public void testSerializable() {
        assertTrue(taskIsSerializable(new AgvTask(sampleOrder, "A", "B", "AGV-01")));
    }

    private boolean taskIsSerializable(AgvTask task) {
        try (var out = new java.io.ByteArrayOutputStream();
                var objOut = new java.io.ObjectOutputStream(out)) {
            objOut.writeObject(task);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
