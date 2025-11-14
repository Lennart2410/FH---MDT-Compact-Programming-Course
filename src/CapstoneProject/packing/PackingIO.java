package CapstoneProject.packing;


import CapstoneProject.logging.LogListener;
import CapstoneProject.packing.exceptions.PackingIoException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PackingIO {
    private final Path base;                 // e.g., Paths.get("logs/packing/PACK-1")
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final List<LogListener> listeners = new ArrayList<>();

    public PackingIO(Path logsRoot) {
        this.base = logsRoot.resolve("PackingStation");
    }

    // PackingIO — add helpers
    public Path getLabelPath(String orderNo) {
        return base.resolve("labels").resolve(orderNo + ".txt");
    }

    public Path getLogPathFor(LocalDate d) {
        return base.resolve(d.format(DAY) + ".log");
    }


    public void addListener(LogListener listener) {
        listeners.add(listener);
    }
    public void searchLogsByLabel(String orderNumber) throws PackingIoException {
        List<Path> labels = null;
        try {
            labels = findByRegex("labels[/\\\\]" + orderNumber + "\\.txt$");
        } catch (IOException e) {
            throw new PackingIoException("Find label failed for order " + orderNumber, e); // chaining
        }
        //labels.forEach(System.out::println);
    }

    public void searchLogsByDate(String date) throws PackingIoException {
        List<Path> hits = null;
        try {
            hits = findByRegex(date + "\\.log$"); // match file ending with that date
        } catch (IOException e) {
            throw new PackingIoException("Find logs by date failed for " + date, e);
        }
        //hits.forEach(System.out::println);
    }

    public void logPacking(String orderNumber, String packerID, List<Parcel> parcels) throws PackingIoException {
        int count = parcels.size();
        double total = parcels.stream().mapToDouble(Parcel::getWeightKg).sum();

        try {
            logEvent(" Packer Machine : " + packerID + " Cartonized " + orderNumber + " parcels=" + count + " totalKg=" + String.format("%.2f", total));
            writeLabel(orderNumber, packerID, count, total);
        } catch (IOException e) {
            throw new PackingIoException("Writing packing artifacts failed for " + orderNumber, e);
        }
    }

    /* -- daily packing log (character stream) ---- */
    public void logEvent(String msg) throws IOException {
        Files.createDirectories(base);
        Path file = base.resolve(LocalDate.now().format(DAY) + ".log");
        try (var w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(TIME.format(LocalTime.now()) + msg);
            w.newLine();
        }
    }

    /* ---- LABEL per order (character stream) ---- */
    public void writeLabel(String orderNo, String packerID, int parcels, double totalKg) throws IOException {
        Path dir = base.resolve("labels");
        Files.createDirectories(dir);
        Path txt = dir.resolve(orderNo + ".txt");
        try (var w = Files.newBufferedWriter(txt, StandardCharsets.UTF_8)) {
            w.write("=== PACKING LABEL ===\n");
            w.write("Packer Machine : " + packerID + "\n");
            w.write("Order   : " + orderNo + "\n");
            w.write("Parcels : " + parcels + "\n");
            w.write(String.format("TotalKg : %.2f%n", totalKg));
            addLogInUi("PackingStation", "=== PACKING LABEL ===");
            addLogInUi("PackingStation", "Packer Machine : " + packerID);
            addLogInUi("PackingStation", "Order   : " + orderNo);
            addLogInUi("PackingStation", "Parcels : " + parcels);
            addLogInUi("PackingStation", String.format("TotalKg : %.2f", totalKg));
        }
    }

    /* ---- Find/open by regex (name/date) ---- */
    public List<Path> findByRegex(String regex) throws IOException {
        Pattern p = Pattern.compile(regex);
        try (var walk = Files.walk(base)) {
            return walk.filter(Files::isRegularFile)
                    .filter(path -> p.matcher(path.toString()).find())
                    .toList();
        }
    }
    public void addLogInUi(String station, String textToLog) {
        for (LogListener l : listeners) {
            l.onLog(station, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": " + textToLog);
        }
    }
}
