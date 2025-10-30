package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.OrderStatusEnum;
import HomeworkAssignment1.general.Station;
import HomeworkAssignment1.packing.exceptions.BoxingFailureException;
import HomeworkAssignment1.packing.exceptions.PackingIoException;
import HomeworkAssignment1.packing.exceptions.PackingProcessException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PackingStation extends Station<PackingTask> {
   private Path logsRoot ;
   private PackingIO packingIo;

    public PackingStation() {
        logsRoot = Paths.get("logs");
        packingIo = new PackingIO(logsRoot);
    }

    /** Extra constructor for tests (inject temp logs and/or custom IO). */
    public PackingStation(Path logsRoot, PackingIO packingIo) {
        this.logsRoot = logsRoot;
        this.packingIo = packingIo;
    }
    @Override
    public Order process(PackingTask packingTask)  {
        // Packing items from the order
        // Do something that the Packing-process would usually cover
        // Process should take some seconds to make it realistic
        // Simulate a tiny bit of work so interrupt is meaningful
        // Simulate a tiny bit of work so interrupt is meaningful
        try { Thread.sleep(50); }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new PackingProcessException("Packing interrupted for " +
                    packingTask.getOrder().getOrderNumber(), ie);   // re-throw + chaining
        }

        // Boxing: if service fails, rethrow with context
        final List<Parcel> parcels;
        try {
            System.out.println("Im im packing!");
            parcels = packingTask.getBoxing().cartonize();
            System.out.println(parcels.getFirst().getId());
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
            throw new PackingProcessException(
                    "I/O sequence failed at packing stage for " +
                            packingTask.getOrder().getOrderNumber(), e);
        }

        return packingTask.getOrder();
    }

    private void LogPackingTask(PackingTask packingTask, List<Parcel> parcels) {
        packingIo.logPacking(packingTask.getOrder().getOrderNumber(), parcels);
        packingIo.searchLogsByDate(java.time.LocalDate.now().toString());
        packingIo.searchLogsByLabel(packingTask.getOrder().getOrderNumber());
        packingIo.exportPackingLog(packingTask.getOrder().getOrderNumber());
    }

}
