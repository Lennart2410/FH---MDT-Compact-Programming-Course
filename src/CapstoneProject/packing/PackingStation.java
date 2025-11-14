package CapstoneProject.packing;


import CapstoneProject.agv.AgvTask;
import CapstoneProject.general.*;
import CapstoneProject.general.exceptions.WarehouseException;
import CapstoneProject.packing.exceptions.BoxingFailureException;
import CapstoneProject.packing.exceptions.PackingIoException;
import CapstoneProject.packing.exceptions.PackingException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PackingStation extends Station<PackingTask> {
    private Path logsRoot;
    private PackingIO packingIo;
    /**
     * Each entry represents a real “packer” (machine).
     */
    private final List<PackingWorker> workers = new ArrayList<>();
    /**
     * Internal pool: one thread per worker.
     */
    private java.util.concurrent.ExecutorService pool;

    public PackingStation(BlockingQueue<Task> in, BlockingQueue<Task> out, OrderStatusListener listener) {
        super(in, out, listener);
        logsRoot = Paths.get("logs");
        packingIo = new PackingIO(logsRoot);
        //  3 packers with different speeds (1.0 = normal, 0.7 = faster)
        workers.add(new PackingWorker("M-1", 10000, 1.0));
        workers.add(new PackingWorker("M-2", 10000, 0.7));
        workers.add(new PackingWorker("M-3", 10000, 0.7));
    }

    /**
     * Extra constructor for tests .
     */
    public PackingStation(Path logsRoot, PackingIO packingIo, BlockingQueue<Task> in, BlockingQueue<Task> out, OrderStatusListener listener) {
        super(in, out, listener);
        this.logsRoot = logsRoot;
        this.packingIo = packingIo;
        // keep one worker so tests work out of the box
        workers.add(new PackingWorker("M-1", 200, 1.0));
    }

    /**
     * Synchronous processing of one PackingTask
     */
    @Override
    public void process(PackingTask packingTask) throws WarehouseException {
        System.out.println("PackingStation starts packing order " + packingTask.getOrder().getOrderNumber() + " with Packer Machine " + packingTask.getPackerID());
        final List<Parcel> parcels;
        try {
            parcels = packingTask.getBoxing().cartonize();
        } catch (RuntimeException ex) {
            // domain wrap with context
            throw new BoxingFailureException("Cartonization failed for " +
                    packingTask.getOrder().getOrderNumber(), ex);
        }

        // update order
        packingTask.getOrder().setOrderParcels(parcels);
        packingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.PACKAGING);
        getListener().onOrderStatusChanged(packingTask.getOrder());

        // I/O sequence: multi-catch -> rethrow as WarehouseException with cause (chaining)
        try {
            LogPackingTask(packingTask, parcels);
        } catch (PackingIoException | IllegalStateException e) {
            throw new WarehouseException(
                    "I/O sequence failed at packing stage for " +
                            packingTask.getOrder().getOrderNumber(), e);
        } catch (PackingException e) {
            throw e; // already a domain exception
        }

        // handoff to AGV (Pack -> Load)
        packingIo.addLogInUi("PackingStation", "PackingStation finished packing for order with order number " + packingTask.getOrder().getOrderNumber() + ". Task enqueued for AGV.");
        addToQueue(new AgvTask(packingTask.getOrder(), "packing-station", "loading-station"));
        System.out.println("PackingStation finished packing for order with order number " + packingTask.getOrder().getOrderNumber() + ". Task enqueued for AGV.");
    }

    private void LogPackingTask(PackingTask packingTask, List<Parcel> parcels) throws PackingException {
        packingIo.logPacking(packingTask.getOrder().getOrderNumber(), packingTask.getPackerID(), parcels);
    }

    /**
     * Starts a fixed-size pool: one thread per packer.
     * Each worker thread pulls from the same IN queue, sets its packerId,
     * simulates speed, then calls process(task) synchronously.
     */
    @Override
    public void run() {
        pool = java.util.concurrent.Executors.newFixedThreadPool(workers.size());
        // Start one thread per worker
        for (PackingWorker w : workers) {
            pool.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // 1) get next task for packing
                        PackingTask task = (PackingTask) in.take();

                        // 2) annotate with this worker id and simulate speed
                        packingIo.addLogInUi("PackingStation", "New Task is received, searching for available packing machine");
                        task.setPackerID(w.getWorkerId());
                        packingIo.addLogInUi("PackingStation", "Packer Machine " + task.getPackerID() + " starts packing order " + task.getOrder().getOrderNumber());
                        Thread.sleep((long) (w.getBaseMs() * w.getSpeedFactor()));

                        // 3) do the real work (synchronous)
                        process(task);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception ex) {
                        // Don’t kill the worker on one bad task; log and continue
                        ex.printStackTrace();
                    }
                }
            });
        }

        // Keep the station thread alive until interrupted; Warehouse executor controls lifecycle
        try {
            // simple idle wait;  Warehouse will control lifecycle with executor shutdown
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(200);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } finally {
            pool.shutdownNow();
        }
    }

    public PackingIO getPackingIo() {
        return packingIo;
    }
}
