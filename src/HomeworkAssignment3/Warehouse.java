package HomeworkAssignment3;


import HomeworkAssignment3.agv.AGVRunner;
import HomeworkAssignment3.agv.AgvTask;
import HomeworkAssignment3.general.Item;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.loading.LoadingStation;
import HomeworkAssignment3.loading.LoadingTask;
import HomeworkAssignment3.logging.LogFiles;
import HomeworkAssignment3.packing.OrderBoxingService;
import HomeworkAssignment3.packing.PackingStation;
import HomeworkAssignment3.packing.PackingTask;
import HomeworkAssignment3.picking.PickingStation;
import HomeworkAssignment3.picking.PickingTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Warehouse {
    PickingStation pickingStation = new PickingStation();
    PackingStation packingStation = new PackingStation();
    AGVRunner agvRunner = new AGVRunner(Path.of("logs"));
    LoadingStation loadingStation = new LoadingStation(2);
    Path logsRoot = Paths.get("logs");
    LogFiles storage = new LogFiles(logsRoot);

    public Warehouse() throws WarehouseException {
        Item item1 = new Item("Phone");
        Item item2 = new Item("Book");
        Item item3 = new Item("Another Book");
        Item item4 = new Item("Pen");

        List<Item> itemList = List.of(item1, item2, item3, item4);

        processOrder(new Order("Example Street 1", itemList));

    }

    public void processOrder(Order order) throws WarehouseException {
        // Picking process from shelves
        order = pickingStation.process(new PickingTask(order, "Shelf-A3", "Yasaman", true));

        // Bringing items from the order to the packaging
        AgvTask toPacking = new AgvTask(order, "shelves", "packing-station", "AGV-01");
        order = agvRunner.process(toPacking);

        // Packaging the items inside the order
        order = packingStation.process(new PackingTask(order, new OrderBoxingService(order), logsRoot));

        // Bringing items from the packaging to the loading
        AgvTask toLoading = new AgvTask(order, "packing-station", "loading-station", "AGV-01");
        order = agvRunner.process(toLoading);

        // Loading items into the delivery vehicle
        loadingStation.process(new LoadingTask(order));

        // HA1: Simulate byte stream for AGV task
        //agvRunner.simulateByteStream(toLoading);

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
