package HomeworkAssignment3.packing;



import HomeworkAssignment3.agv.AgvTask;
import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.OrderStatusEnum;
import HomeworkAssignment3.general.Station;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.packing.exceptions.BoxingFailureException;
import HomeworkAssignment3.packing.exceptions.PackingIoException;
import HomeworkAssignment3.packing.exceptions.PackingException;
import SelfAssignment3.AGV;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PackingStation extends Station<PackingTask> {
   private Path logsRoot ;
   private PackingIO packingIo;

    public PackingStation(BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in, out);
        logsRoot = Paths.get("logs");
        packingIo = new PackingIO(logsRoot);
    }

    /** Extra constructor for tests (inject temp logs and/or custom IO). */
    public PackingStation(Path logsRoot, PackingIO packingIo, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in, out);
        this.logsRoot = logsRoot;
        this.packingIo = packingIo;
    }
    @Override
    public void process(PackingTask packingTask) throws WarehouseException {
        System.out.println("PackingStation starts packing.");

        // Boxing: if service fails, rethrow with context
        final List<Parcel> parcels;
        try {
            parcels = packingTask.getBoxing().cartonize();
        } catch (RuntimeException ex) {
            throw new BoxingFailureException("Cartonization failed for " +
                    packingTask.getOrder().getOrderNumber(), ex);
        }

        // Update order state
        packingTask.getOrder().setOrderParcels(parcels);
        packingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.PACKAGING);

        // I/O sequence: multi-catch and rethrow as processing error
        try {
            LogPackingTask(packingTask, parcels);
        } catch (PackingIoException | IllegalStateException e) { // multiple exceptions
            throw new WarehouseException(
                    "I/O sequence failed at packing stage for " +
                            packingTask.getOrder().getOrderNumber(), e);
        }
        System.out.println("PackingStation is finished packing into parcels. Putting task into agv queue.");
        addToQueue(new AgvTask(packingTask.getOrder(), "packing-station", "loading-station", "AGV-01"));
    }

    private void LogPackingTask(PackingTask packingTask, List<Parcel> parcels) throws PackingException {
        packingIo.logPacking(packingTask.getOrder().getOrderNumber(), parcels);
        packingIo.searchLogsByDate(java.time.LocalDate.now().toString());
        packingIo.searchLogsByLabel(packingTask.getOrder().getOrderNumber());
        packingIo.exportPackingLog(packingTask.getOrder().getOrderNumber());
    }

}
