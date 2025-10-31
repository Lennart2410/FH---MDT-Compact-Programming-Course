package HomeworkAssignment2.packing;

import HomeworkAssignment2.packing.Parcel;
import HomeworkAssignment2.packing.exceptions.PackingIoException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class PackingIO {
    private final Path base;                 // e.g., Paths.get("logs/packing/PACK-1")
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public PackingIO(Path logsRoot) {
        this.base = logsRoot.resolve("packing");
    }
    // PackingIO — add helpers
    public Path getLabelPath(String orderNo)   { return base.resolve("labels").resolve(orderNo + ".txt"); }
    public Path getManifestPath(String orderNo){ return base.resolve("manifests").resolve(orderNo + ".bin"); }
    public Path getLogPathFor(LocalDate d)     { return base.resolve(d.format(DAY) + ".log"); }
    public Path getExportPath(String orderNo)  { return base.getParent().resolve("exports").resolve(orderNo + ".txt"); }

    public void searchLogsByLabel(String orderNumber) {
        List<Path> labels = null;
        try {
            labels = findByRegex("labels[/\\\\]"+orderNumber+"\\.txt$");
        } catch (IOException e) {
            throw new PackingIoException("Find label failed for order "+orderNumber, e); // chaining
        }
        labels.forEach(System.out::println);
    }

    public void searchLogsByDate(String date) {
        List<Path> hits = null;
        try {
            hits = findByRegex(date + "\\.log$"); // match file ending with that date
        } catch (IOException e) {
            throw new PackingIoException("Find logs by date failed for "+date, e);
        }
        hits.forEach(System.out::println);
    }

    public Path exportPackingLog(String orderNumber){
        Path label = base.resolve("labels").resolve(orderNumber + ".txt");     // source
        Path dest  = base.getParent().resolve("exports").resolve(orderNumber + ".txt"); // logs/exports/...
        try {
            Files.createDirectories(dest.getParent());
            // COPY instead of MOVE
            Files.copy(label, dest, StandardCopyOption.REPLACE_EXISTING);
            return dest;
        } catch (IOException e) {
            throw new PackingIoException(
                    "Export failed: " + label + " → " + dest, e);
        }
    }
    public void logPacking(String orderNumber, List<Parcel> parcels) {
        int count = parcels.size();
        double total = parcels.stream().mapToDouble(Parcel::getWeightKg).sum();

        try {
            logEvent("Cartonized " + orderNumber + " parcels=" + count + " totalKg=" + String.format("%.2f", total));
            writeLabel(orderNumber, count, total);
        } catch (IOException e) {
            throw new PackingIoException("Writing packing artifacts failed for "+orderNumber, e);        }
    }
    /* -- daily packing log (character stream) ---- */
    public void logEvent(String msg) throws IOException {
        Files.createDirectories(base);
        Path file = base.resolve(LocalDate.now().format(DAY) + ".log");
        try (var w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(TIME.format(LocalTime.now()) + ",PACK," + msg);
            w.newLine();
        }
    }

    /* ---- LABEL per order (character stream) ---- */
    public void writeLabel(String orderNo, int parcels, double totalKg) throws IOException {
        Path dir = base.resolve("labels");
        Files.createDirectories(dir);
        Path txt = dir.resolve(orderNo + ".txt");
        try (var w = Files.newBufferedWriter(txt, StandardCharsets.UTF_8)) {
            w.write("=== PACKING LABEL ===\n");
            w.write("Order   : " + orderNo + "\n");
            w.write("Parcels : " + parcels + "\n");
            w.write(String.format("TotalKg : %.2f%n", totalKg));
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

    /* ---- Manage files: move, delete, archive ---- */
    public void move(Path src, Path dst) throws IOException {
        Files.createDirectories(dst.getParent());
        Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    public void delete(Path p) throws IOException {
        Files.deleteIfExists(p);
    }

    public void archive(Path src, Path zip) throws IOException {
        Files.createDirectories(zip.getParent());
        try (var zos = new java.util.zip.ZipOutputStream(Files.newOutputStream(zip))) {
            zos.putNextEntry(new java.util.zip.ZipEntry(src.getFileName().toString()));
            Files.copy(src, zos);
            zos.closeEntry();
        }
    }
}
