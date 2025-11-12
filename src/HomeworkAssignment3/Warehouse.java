package HomeworkAssignment3;


import HomeworkAssignment3.agv.AGVRunner;
import HomeworkAssignment3.general.Item;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.loading.LoadingStation;
import HomeworkAssignment3.logging.LogFiles;
import HomeworkAssignment3.packing.PackingStation;
import HomeworkAssignment3.picking.PickingStation;
import HomeworkAssignment3.picking.PickingTask;

import java.nio.file.Path;
import java.nio.file.Paths;
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
    LoadingStation loadingStation = new LoadingStation(2, ingoingQueueLoading, null);
    Path logsRoot = Paths.get("logs");
    LogFiles storage = new LogFiles(logsRoot);

    public Warehouse() throws WarehouseException {
        storage.writeLogEntry("Hello World","Warehouse");


        ExecutorService exec = Executors.newFixedThreadPool(5);
        exec.submit(pickingStation);
        exec.submit(agvRunnerPickToPack);
        exec.submit(packingStation);
        exec.submit(agvRunnerPackToLoad);
        exec.submit(loadingStation);
        System.out.println("Warehouse systems starts");
        //processOrder(new Order("Example Street 1", itemList));
    }

    public void processOrder(Order order) throws WarehouseException {
        // Picking process from shelves
        ingoingQueuePicking.add(new PickingTask(order, "Shelf-A3",  true));
    }


    public PickingStation getPickingStation() {
        return pickingStation;
    }

    public AGVRunner getAgvRunnerPickToPack() {
        return agvRunnerPickToPack;
    }

    public PackingStation getPackingStation() {
        return packingStation;
    }

    public AGVRunner getAgvRunnerPackToLoad() {
        return agvRunnerPackToLoad;
    }

    public LoadingStation getLoadingStation() {
        return loadingStation;
    }
}
