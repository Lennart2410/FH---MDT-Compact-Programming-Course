/*package HomeworkAssignment1;


import HomeworkAssignment1.agv.AGVRunner;
import HomeworkAssignment1.agv.AgvTask;
import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.loading.LoadingStation;
import HomeworkAssignment1.loading.LoadingTask;
import HomeworkAssignment1.logging.LogFiles;
import HomeworkAssignment1.packing.OrderBoxingService;
import HomeworkAssignment1.packing.PackingStation;
import HomeworkAssignment1.packing.PackingTask;
import HomeworkAssignment1.picking.PickingStation;
import HomeworkAssignment1.picking.PickingTask;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Warehouse {
    PickingStation pickingStation = new PickingStation();
    PackingStation packingStation  = new PackingStation();
    AGVRunner agvRunner = new AGVRunner();
    LoadingStation loadingStation  = new LoadingStation();
    Path logsRoot = Paths.get("logs");
    LogFiles storage = new LogFiles(logsRoot);
    public Warehouse() {
        Item item1 = new Item("Phone");
        Item item2 = new Item("Book");
        Item item3 = new Item("Another Book");
        Item item4 = new Item("Pen");

        List<Item> itemList = List.of(item1, item2, item3, item4);

        processOrder(new Order("Example Street 1", itemList));

    }

    public void processOrder(Order order){
        // Picking process from shelves
        order = pickingStation.process(new PickingTask(order));

        // Bringing items from the order to the packaging
        order = agvRunner.process(new AgvTask(order,"shelves","packing-station"));

        // Packaging the items inside the order
        order = packingStation.process(new PackingTask(order, new OrderBoxingService(order), storage, logsRoot));
        List<Path> hits = null;
        try {
            hits = storage.findLogsByRegex("packing[/\\\\]PACK-1[/\\\\]" + java.time.LocalDate.now() + "\\.log$");
            hits.forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

// Any label for order A100:
        List<Path> labels = null;
        try {
            labels = storage.findLogsByRegex("packing[/\\\\]PACK-1[/\\\\]labels[/\\\\]A100\\.txt$");
            labels.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Bringing items from the packaging to the loading
        // Note: to access the parcel from packaging station, use order.getOrderParcels()
        order = agvRunner.process(new AgvTask(order,"packing-station","loading-station"));

        // Loading items into the delivery vehicle
        loadingStation.process(new LoadingTask(order));
    }

} */

package HomeworkAssignment1;

import HomeworkAssignment1.agv.AGVRunner;
import HomeworkAssignment1.agv.AgvTask;
import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.loading.LoadingStation;
import HomeworkAssignment1.loading.LoadingTask;
import HomeworkAssignment1.packing.PackingStation;
import HomeworkAssignment1.packing.PackingTask;
import HomeworkAssignment1.picking.PickingStation;
import HomeworkAssignment1.picking.PickingTask;
import HomeworkAssignment1.logging.LogFiles;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Warehouse {
    PickingStation pickingStation = new PickingStation();
    PackingStation packingStation = new PackingStation();
    AGVRunner agvRunner = new AGVRunner(Path.of("logs"));
    LoadingStation loadingStation = new LoadingStation();

    public Warehouse() {
        Item item1 = new Item("Phone");
        Item item2 = new Item("Book");
        Item item3 = new Item("Another Book");
        Item item4 = new Item("Pen");

        List<Item> itemList = List.of(item1, item2, item3, item4);

        processOrder(new Order("Example Street 1", itemList));
    }

    public void processOrder(Order order) {
        // Picking process from shelves
        order = pickingStation.process(new PickingTask(order));

        // Bringing items from the order to the packaging
        AgvTask toPacking = new AgvTask(order, "shelves", "packing-station", "AGV-01");
        order = agvRunner.process(toPacking);

        // Packaging the items inside the order
        order = packingStation.process(new PackingTask(order));

        // Bringing items from the packaging to the loading
        AgvTask toLoading = new AgvTask(order, "packing-station", "loading-station", "AGV-01");
        order = agvRunner.process(toLoading);

        // Loading items into the delivery vehicle
        loadingStation.process(new LoadingTask(order));

        // HA1: Simulate byte stream for AGV task
        agvRunner.simulateByteStream(toLoading);

        // HA1: Archive today's AGV log
        try {
            LogFiles logFiles = agvRunner.getLogFiles(); // Reuse existing instance
            Path logToArchive = logFiles.pathFor("AGV", "AGV-01", LocalDate.now());
            logFiles.archiveLog(logToArchive);
        } catch (Exception e) {
            System.err.println("Archiving failed: " + e.getMessage());
        }

        // HA1: System-wide log entry
        try {
            LogFiles logFiles = agvRunner.getLogFiles();
            Path systemLog = logFiles.pathFor("System", null, LocalDate.now());
            logFiles.appendLine(systemLog, "Order processed from shelves to loading");
        } catch (Exception e) {
            System.err.println("System log failed: " + e.getMessage());
        }
    }
}
