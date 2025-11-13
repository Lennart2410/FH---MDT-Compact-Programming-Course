package HomeworkAssignment3;


import HomeworkAssignment3.agv.AGVRunner;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.OrderStatusEnum;
import HomeworkAssignment3.general.OrderStatusListener;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.loading.LoadingStation;
import HomeworkAssignment3.logging.LogFiles;
import HomeworkAssignment3.packing.PackingStation;
import HomeworkAssignment3.picking.PickingStation;
import HomeworkAssignment3.picking.PickingTask;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Warehouse implements OrderStatusListener{
    BlockingQueue<Task> ingoingQueuePicking =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueueAGVPickToPack =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueueAGVPackToLoad =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueuePacking =  new ArrayBlockingQueue<>(1);
    BlockingQueue<Task>  ingoingQueueLoading =  new ArrayBlockingQueue<>(1);

    PickingStation pickingStation = new PickingStation(ingoingQueuePicking, ingoingQueueAGVPickToPack,this);
    AGVRunner agvRunnerPickToPack = new AGVRunner(Path.of("logs"),ingoingQueueAGVPickToPack,ingoingQueuePacking,this);
    PackingStation packingStation = new PackingStation(ingoingQueuePacking, ingoingQueueAGVPackToLoad,this);
    AGVRunner agvRunnerPackToLoad = new AGVRunner(Path.of("logs"),ingoingQueueAGVPackToLoad,ingoingQueueLoading,this);
    LoadingStation loadingStation = new LoadingStation(2, ingoingQueueLoading, null,this);

    LogFiles storage = new LogFiles();

    private final List<OrderStatusListener> statusListeners = new ArrayList<>();

    public Warehouse() throws WarehouseException {


        ExecutorService exec = Executors.newFixedThreadPool(5);
        exec.submit(pickingStation);
        exec.submit(agvRunnerPickToPack);
        exec.submit(packingStation);
        exec.submit(agvRunnerPackToLoad);
        exec.submit(loadingStation);

        storage.writeLogEntry("Hello World","Warehouse");
    }

    public void processOrder(Order order) throws WarehouseException {
        // Picking process from shelves
        order.setOrderStatusEnum(OrderStatusEnum.ORDERED);
        notifyStatusChanged(order);
        ingoingQueuePicking.add(new PickingTask(order, "Shelf-A3",  true));
    }

    public void addOrderStatusListener(OrderStatusListener listener) {
        statusListeners.add(listener);
    }

    private void notifyStatusChanged(Order order) {
        onOrderStatusChanged(order);
    }


    @Override
    public void onOrderStatusChanged(Order order) {
        // Forward to all registered listeners (e.g. the UI)
        for (OrderStatusListener listener : statusListeners) {
            listener.onOrderStatusChanged(order);
        }
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
