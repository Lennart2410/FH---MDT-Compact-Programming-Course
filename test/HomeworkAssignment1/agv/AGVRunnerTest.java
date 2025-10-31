package HomeworkAssignment1.agv;

import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AGVRunnerTest {

    AGVRunner runner = new AGVRunner(Path.of("logs"));
    Order order = new Order("Test", List.of(new Item("Book")));

    @Test
    public void testProcessReturnsOrder() {
        AgvTask task = new AgvTask(order, "A", "B", "AGV-01");
        assertEquals(order, runner.process(task));
    }

    @Test
    public void testSimulateByteStream() {
        AgvTask task = new AgvTask(order, "A", "B", "AGV-01");
        assertDoesNotThrow(() -> runner.simulateByteStream(task));
    }

    @Test
    public void testSimulateFailureThrowsChainedException() {
        RuntimeException ex = assertThrows(RuntimeException.class, runner::simulateFailure);
        assertTrue(ex.getMessage().contains("Transport failed"));
        assertNotNull(ex.getCause());
    }

    @Test
    public void testGetLogFilesNotNull() {
        assertNotNull(runner.getLogFiles());
    }

    @Test
    public void testProcessHandlesInterruptedException() {
        Thread.currentThread().interrupt(); // Simulate interruption
        AgvTask task = new AgvTask(order, "A", "B", "AGV-01");
        assertThrows(RuntimeException.class, () -> runner.process(task));
    }

}
