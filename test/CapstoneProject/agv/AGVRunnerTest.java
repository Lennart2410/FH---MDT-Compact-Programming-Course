package CapstoneProject.agv;


import CapstoneProject.agv.exceptions.AGVException;
import CapstoneProject.general.*;
import CapstoneProject.general.exceptions.WarehouseException;
import CapstoneProject.loading.LoadingStation;
import CapstoneProject.loading.exceptions.NoDestinationException;
import CapstoneProject.loading.vehicles.Truck;
import CapstoneProject.loading.vehicles.Van;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class AGVRunnerTest {

    AGVRunner runner ;
    Order order;
    BlockingQueue<Task> ingoingQueue;
    BlockingQueue<Task> outgoingQueue;

    @BeforeEach
    void setUp() throws AGVException {
        ingoingQueue = new ArrayBlockingQueue<>(1);
        outgoingQueue = new ArrayBlockingQueue<>(1);

        runner = new AGVRunner(Path.of("logs"), ingoingQueue, outgoingQueue, null);
        order = new Order("Test", List.of(new Item("Book")));
    }

    @Test
    public void testProcessReturnsOrder() {
        AgvTask task = new AgvTask(order, "A", "B");
        //assertEquals(order, runner.process(task));
    }

    @Test
    public void testSimulateByteStream() {
        AgvTask task = new AgvTask(order, "A", "B");
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
    public void testProcessOrderStatusException() throws WarehouseException, InterruptedException {
        AgvTask task = new AgvTask(order, "A", "B");
        task.setAgv(new AGV("AGV01"));
        runner.process(task);
        Thread.sleep(2500);
        assertEquals(order.getOrderStatusEnum(), OrderStatusEnum.EXCEPTION);
    }

}
