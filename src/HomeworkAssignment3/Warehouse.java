package HomeworkAssignment3;


import HomeworkAssignment3.agv.AGVRunner;
import HomeworkAssignment3.agv.AgvTask;
import HomeworkAssignment3.general.Item;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Task;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Warehouse {
    BlockingQueue<Task> ingoingQueuePicking =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueueAGVPickToPack =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueueAGVPackToLoad =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueuePacking =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueueLoading =  new ArrayBlockingQueue<>(1);

    PickingStation pickingStation = new PickingStation(ingoingQueuePicking, ingoingQueueAGVPickToPack);
    AGVRunner agvRunnerPickToPack = new AGVRunner(Path.of("logs"),ingoingQueueAGVPickToPack,ingoingQueuePacking);
    PackingStation packingStation = new PackingStation(ingoingQueuePacking, ingoingQueueAGVPackToLoad);
    AGVRunner agvRunnerPackToLoad = new AGVRunner(Path.of("logs"),ingoingQueueAGVPackToLoad,ingoingQueueLoading);
    LoadingStation loadingStation = new LoadingStation(2, ingoingQueuePacking, null);
    Path logsRoot = Paths.get("logs");
    LogFiles storage = new LogFiles(logsRoot);

    public Warehouse() throws WarehouseException {
        Item item1 = new Item("Phone");
        Item item2 = new Item("Book");
        Item item3 = new Item("Another Book");
        Item item4 = new Item("Pen");

        List<Item> itemList = List.of(item1, item2, item3, item4);

        ExecutorService exec = Executors.newFixedThreadPool(5);
        exec.submit(pickingStation);
        exec.submit(agvRunnerPickToPack);
        exec.submit(packingStation);
        exec.submit(agvRunnerPackToLoad);
        exec.submit(loadingStation);
        System.out.println("Warehouse systems starts");
        processOrder(new Order("Example Street 1", itemList));
    }

    public void processOrder(Order order) throws WarehouseException {
        // Picking process from shelves
        ingoingQueuePicking.add(new PickingTask(order, "Shelf-A3", "Yasaman", true));
    }
}
