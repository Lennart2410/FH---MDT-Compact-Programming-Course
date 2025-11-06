package HomeworkAssignment3.packing;



import HomeworkAssignment3.agv.AgvTask;
import HomeworkAssignment3.general.*;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.packing.exceptions.BoxingFailureException;
import HomeworkAssignment3.packing.exceptions.PackingIoException;
import HomeworkAssignment3.packing.exceptions.PackingException;
import HomeworkAssignment3.picking.PickingTask;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PackingStation extends Station<PackingTask> {
   private Path logsRoot ;
   private PackingIO packingIo;
   private final List<PackingWorker> workers = new ArrayList<>();

    public PackingStation(BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in, out);
        logsRoot = Paths.get("logs");
        packingIo = new PackingIO(logsRoot);
        workers.add(new PackingWorker("M-1", 200, 1.0));
        workers.add(new PackingWorker("M-2", 200, 0.7));
        workers.add(new PackingWorker("M-3", 200, 0.7));
    }

    /** Extra constructor for tests (inject temp logs and/or custom IO). */
    public PackingStation(Path logsRoot, PackingIO packingIo, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in, out);
        this.logsRoot = logsRoot;
        this.packingIo = packingIo;
    }
    @Override
    public void process(PackingTask packingTask) throws WarehouseException {
        new Thread(() -> {
        System.out.println("PackingStation starts packing with packer " +packingTask.getPackerID());
            try {

        final List<Parcel> parcels;
        try {
            Thread.sleep(10000);
            parcels = packingTask.getBoxing().cartonize();
            // Update order state
            packingTask.getOrder().setOrderParcels(parcels);
            packingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.PACKAGING);
            LogPackingTask(packingTask, parcels);
            System.out.println("PackingStation is finished packing into parcels. Putting task into agv queue.");
            addToQueue(new AgvTask(packingTask.getOrder(), "packing-station", "loading-station", "AGV-01"));

        } catch (RuntimeException ex) {    // Boxing: if service fails, rethrow with context
            throw new BoxingFailureException("Cartonization failed for " +
                    packingTask.getOrder().getOrderNumber(), ex);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
            } catch (PackingIoException | IllegalStateException e) { // multiple exceptions
                try {
                    throw new WarehouseException(
                            "I/O sequence failed at packing stage for " +
                                    packingTask.getOrder().getOrderNumber(), e);
                } catch (WarehouseException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (PackingException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void LogPackingTask(PackingTask packingTask, List<Parcel> parcels) throws PackingException {
        packingIo.logPacking(packingTask.getOrder().getOrderNumber(),packingTask.getPackerID(), parcels);
        packingIo.searchLogsByDate(java.time.LocalDate.now().toString());
        packingIo.searchLogsByLabel(packingTask.getOrder().getOrderNumber());
        packingIo.exportPackingLog(packingTask.getOrder().getOrderNumber());
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // checks if a packer is currently available for the task
                PackingWorker packer = retrievePossibleWorker();
                if (packer != null) {
                    // If a packer is available
                    PackingTask task = (PackingTask) in.take();
                    task.setPackerID(packer.getWorkerId());
                    packer.setCurrentlyOccupied(true);
                    // start the concrete process
                    process(task);
                    // when the process / thread is finished, set the packer to unassigned.
                    packer.setCurrentlyOccupied(false);
                } else {
                    System.out.println("No available Packer. Task will wait.");
                }
            }
        } catch (InterruptedException stop) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // As a safety net, if a station throws, convert task to EXCEPTION
            e.printStackTrace();
        }
    }
    public PackingWorker retrievePossibleWorker() {
        for (PackingWorker worker : workers) {
            if (!worker.isCurrentlyOccupied()) {
                return worker;
            }
        }
        return null;
    }
}
