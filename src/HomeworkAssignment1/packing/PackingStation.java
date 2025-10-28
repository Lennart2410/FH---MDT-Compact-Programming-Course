package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.OrderStatusEnum;
import HomeworkAssignment1.general.Station;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PackingStation extends Station<PackingTask> {
    Path logsRoot = Paths.get("logs");
    PackingIo packingIo = new PackingIo(logsRoot);
    @Override
    public Order process(PackingTask packingTask)  {
        // Packing items from the order
        // Do something that the Packing-process would usually cover
        // Process should take some seconds to make it realistic

        System.out.println("Im im packing!");
        var parcels = packingTask.getBoxing().cartonize();
        System.out.println(parcels.getFirst().getId());
        packingTask.getOrder().setOrderParcels(parcels);
        packingTask.getOrder().setOrderStatusEnum(OrderStatusEnum.PACKAGING);
        logPacking(packingTask, parcels);
        getLogbyDate(java.time.LocalDate.now().toString());
        getLogByLabel(packingTask.getOrder().getOrderNumber());
        exportPackingLog(packingTask.getOrder().getOrderNumber());
        return packingTask.getOrder();
    }

    private void getLogByLabel(String orderNumber) {
        List<Path> labels = null;
        try {
            labels = packingIo.findByRegex("labels[/\\\\]"+orderNumber+"\\.txt$");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        labels.forEach(System.out::println);
    }

    private void getLogbyDate(String date) {
        List<Path> hits = null;
        try {
            hits = packingIo.findByRegex(date + "\\.log$"); // match file ending with that date
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hits.forEach(System.out::println);
    }

    private void exportPackingLog(String orderNumber){
// move label to an “export” folder
    Path label = Paths.get("logs/packing/labels/"+orderNumber+".txt");
    try {
        packingIo.move(label, Paths.get("exports/"+orderNumber+".txt"));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    private void logPacking(PackingTask packingTask, List<Parcel> parcels) {
        int count = parcels.size();
        double total = parcels.stream().mapToDouble(Parcel::getWeightKg).sum();

        try {
            packingIo.logEvent("Cartonized " + packingTask.getOrder().getOrderNumber() + " parcels=" + count + " totalKg=" + String.format("%.2f", total));
            packingIo.writeLabel(packingTask.getOrder().getOrderNumber(), count, total);
            packingIo.writeManifest(packingTask.getOrder().getOrderNumber(), count, total);
        } catch (IOException e) {
            System.err.println("PACK I/O failed: " + e.getMessage());
        }
    }

}
