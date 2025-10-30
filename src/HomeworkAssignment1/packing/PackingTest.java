package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.OrderStatusEnum;
import HomeworkAssignment1.logging.LogFiles;
import HomeworkAssignment1.packing.exceptions.BoxingFailureException;
import HomeworkAssignment1.packing.exceptions.PackingIoException;
import HomeworkAssignment1.packing.exceptions.PackingProcessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackingTest {
    Path temp;
    PackingIO io;

    @BeforeEach
    void setUp() throws Exception {
        temp = Files.createTempDirectory("pack-logs-");
        io   = new PackingIO(temp); // logs root = temp
    }

    @AfterEach
    void tearDown() throws Exception {
        try (var walk = Files.walk(temp)) {
            walk.sorted((a,b)->b.getNameCount()-a.getNameCount())
                    .forEach(p -> { try { Files.deleteIfExists(p); } catch (Exception ignored) {} });
        }
    }

    private Order newOrder() {
        return new Order("DUS", List.of(new Item("Book"), new Item("Cable")));
    }

    /** 1) Happy path: files written, status set, export created. */
    @Test
    void process_success_createsPackingLogsAndSetsStatus() {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-"+order.getOrderNumber(), "S", 1.2));
        PackingTask task = new PackingTask(order, ok, null, temp);

        PackingStation station = new PackingStation(temp, io);
        Order out = station.process(task);

        assertEquals(OrderStatusEnum.PACKAGING, out.getOrderStatusEnum());

        Path logFile     = io.getLogPathFor(LocalDate.now());
        Path exportedLbl = io.getExportPath(order.getOrderNumber());
        Path labelPath = io.getLabelPath(order.getOrderNumber());


        assertTrue(Files.exists(logFile));
        assertTrue(Files.exists(labelPath));
        assertTrue(Files.exists(exportedLbl));        // <— check exported
    }

    /** 2) Boxing failure is wrapped as BoxingFailureException. */
    @Test
    void process_boxingThrows_wrapsAsBoxingFailureException() {
        Order order = newOrder();
        BoxingService failing = () -> { throw new IllegalStateException("scale offline"); };
        PackingTask task = new PackingTask(order, failing, null, temp);

        PackingStation station = new PackingStation(temp, io);
        BoxingFailureException ex = assertThrows(BoxingFailureException.class, () -> station.process(task));
        assertTrue(ex.getMessage().contains("Cartonization failed"));
        assertNotNull(ex.getCause());
    }

    /** 3) Export failure triggers multi-catch and rethrow as PackingProcessException. */
    @Test
    void process_exportFails_rethrowsAsPackingProcessException() {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-"+order.getOrderNumber(),"S",1.0));
        PackingTask task = new PackingTask(order, ok, null, temp);

        // IO test double: make only export fail
        PackingIO ioFailingExport = new PackingIO(temp) {
            @Override public Path exportPackingLog(String orderNumber) {
                throw new PackingIoException("simulated export failure", new RuntimeException("fail"));
            }
        };

        PackingStation station = new PackingStation(temp, ioFailingExport);
        PackingProcessException ex = assertThrows(PackingProcessException.class, () -> station.process(task));
        assertNotNull(ex.getCause());
        assertTrue(ex.getMessage().contains("I/O sequence failed"));
    }

    /** 4) label readable immediately (stream closed). */
    @Test
    void process_labelReadableAfterWrite() throws Exception {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-"+order.getOrderNumber(),"S",1.0));
        PackingTask task = new PackingTask(order, ok, null, temp);

        PackingStation station = new PackingStation(temp, io);
        station.process(task);

        Path label = temp.resolve("packing")
                .resolve("labels")
                .resolve(order.getOrderNumber() + ".txt");
        List<String> lines = Files.readAllLines(label);
        assertEquals("=== PACKING LABEL ===", lines.get(0));
    }

    /** 5) Interrupted thread is rethrown as PackingProcessException (rethrow + chaining). */
    @Test
    void process_interruptedThread_rethrowsAsPackingProcessException() {
        Order order = newOrder();
        BoxingService ok = () -> List.of(new Parcel("P-"+order.getOrderNumber(),"S",1.0));
        PackingTask task = new PackingTask(order, ok, null, temp);

        PackingStation station = new PackingStation(temp, io);

        Thread.currentThread().interrupt(); // cause next sleep to throw immediately
        try {
            assertThrows(PackingProcessException.class, () -> station.process(task));
        } finally {
            Thread.interrupted(); // clear flag
        }
    }

}
