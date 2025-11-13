package CapstoneProject;


import CapstoneProject.agv.AGVRunner;
import CapstoneProject.general.Order;
import CapstoneProject.general.OrderStatusEnum;
import CapstoneProject.general.OrderStatusListener;
import CapstoneProject.general.Task;
import CapstoneProject.general.exceptions.WarehouseException;
import CapstoneProject.loading.LoadingStation;
import CapstoneProject.logging.LogFiles;
import CapstoneProject.packing.PackingStation;
import CapstoneProject.picking.PickingStation;
import CapstoneProject.picking.PickingTask;

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
