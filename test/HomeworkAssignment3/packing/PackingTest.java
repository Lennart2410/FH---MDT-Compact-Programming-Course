package HomeworkAssignment3.packing;

import HomeworkAssignment3.general.Item;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.OrderStatusEnum;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.packing.exceptions.PackingIoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class PackingTest {
    Path temp;
    PackingIO io;
    PackingStation station;
    BlockingQueue<Task> ingoingQueue;
    BlockingQueue<Task> outgoingQueue;

    @BeforeEach
    void setUp() throws Exception {
        ingoingQueue = new ArrayBlockingQueue<>(1);
        outgoingQueue = new ArrayBlockingQueue<>(1);

        temp = Files.createTempDirectory("pack-logs-");
        io = new PackingIO(temp); // logs root = temp

        station = new PackingStation(temp, io, ingoingQueue, outgoingQueue);
    }

    @AfterEach
    void tearDown() throws Exception {
        try (var walk = Files.walk(temp)) {
            walk.sorted((a, b) -> b.getNameCount() - a.getNameCount())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (Exception ignored) {
                        }
                    });
        }
    }

    private Order newOrder() {
        return new Order("DUS", List.of(new Item("Book"), new Item("Cable")));
    }

    /**
     * 1) Happy path: files written, status set, export created.
     */
    @Test
    void process_success_createsPackingLogsAndSetsStatus() throws WarehouseException {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-" + order.getOrderNumber(), "S", 1.2));
        PackingTask task = new PackingTask(order);
        task.setPackerID("M-1");

        station.process(task);

        assertEquals(OrderStatusEnum.PACKAGING, order.getOrderStatusEnum());

        Path logFile = io.getLogPathFor(LocalDate.now());
        Path exportedLbl = io.getExportPath(order.getOrderNumber());
        Path labelPath = io.getLabelPath(order.getOrderNumber());


        assertTrue(Files.exists(logFile));
        assertTrue(Files.exists(labelPath));
        assertTrue(Files.exists(exportedLbl));        // <— check exported
    }

    /**
     * 2) Boxing failure is wrapped as BoxingFailureException.
     */
    @Test
    void process_boxingThrows_wrapsAsBoxingFailureException() {
        Order order = newOrder();
        BoxingService failing = () -> {
            throw new IllegalStateException("scale offline");
        };
        PackingTask task = new PackingTask(order);
        task.setPackerID("M-1");

        PackingStation station = new PackingStation(temp, io, ingoingQueue, outgoingQueue);
        WarehouseException ex = assertThrows(WarehouseException.class, () -> station.process(task));
        assertTrue(ex.getMessage().contains("Cartonization failed"));
        assertNotNull(ex.getCause());
    }

    /**
     * 3) Export failure triggers multi-catch and rethrow as PackingProcessException.
     */
    @Test
    void process_exportFails_rethrowsAsWarehouseException() {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-" + order.getOrderNumber(), "S", 1.0));
        PackingTask task = new PackingTask(order);
        task.setPackerID("M-1");

        // IO test double: make only export fail
        PackingIO ioFailingExport = new PackingIO(temp) {
            @Override
            public Path exportPackingLog(String orderNumber) throws PackingIoException {
                throw new PackingIoException("simulated export failure", new RuntimeException("fail"));
            }
        };

        PackingStation station = new PackingStation(temp, ioFailingExport,  ingoingQueue, outgoingQueue);
        WarehouseException ex = assertThrows(WarehouseException.class, () -> station.process(task));
        assertNotNull(ex.getCause());
        assertTrue(ex.getMessage().contains("I/O sequence failed"));
    }

    /**
     * 4) label readable immediately (stream closed).
     */
    @Test
    void process_labelReadableAfterWrite() throws Exception {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-" + order.getOrderNumber(), "S", 1.0));
        PackingTask task = new PackingTask(order);
        task.setPackerID("M-1");
        PackingStation station = new PackingStation(temp, io, ingoingQueue, outgoingQueue);
        station.process(task);

        Path label = temp.resolve("PackingStation")
                .resolve("labels")
                .resolve(order.getOrderNumber() + ".txt");
        List<String> lines = Files.readAllLines(label);
        assertEquals("=== PACKING LABEL ===", lines.get(0));
    }

}
