package HomeworkAssignment1.packing;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public final class PackingIo {
    private final Path base;                 // e.g., Paths.get("logs/packing/PACK-1")
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public PackingIo(Path logsRoot) {
        this.base = logsRoot.resolve("packing");
    }

    /* ---- TEXT daily log (character stream) ---- */
    public Path logEvent(String msg) throws IOException {
        Files.createDirectories(base);
        Path file = base.resolve(LocalDate.now().format(DAY) + ".log");
        try (var w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(TIME.format(LocalTime.now()) + ",PACK," + msg);
            w.newLine();
        }
        return file;
    }

    /* ---- LABEL per order (character stream) ---- */
    public Path writeLabel(String orderNo, int parcels, double totalKg) throws IOException {
        Path dir = base.resolve("labels");
        Files.createDirectories(dir);
        Path txt = dir.resolve(orderNo + ".txt");
        try (var w = Files.newBufferedWriter(txt, StandardCharsets.UTF_8)) {
            w.write("=== PACKING LABEL ===\n");
            w.write("Order   : " + orderNo + "\n");
            w.write("Parcels : " + parcels + "\n");
            w.write(String.format("TotalKg : %.2f%n", totalKg));
        }
        return txt;
    }

    /* ---- BINARY manifest per order (byte stream) ---- */
    public Path writeManifest(String orderNo, int parcels, double totalKg) throws IOException {
        Path dir = base.resolve("manifests");
        Files.createDirectories(dir);
        Path bin = dir.resolve(orderNo + ".bin");
        try (var out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(
                bin, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)))) {
            // [UTF orderNo][int parcels][double totalKg]
            out.writeUTF(orderNo);
            out.writeInt(parcels);
            out.writeDouble(totalKg);
        }
        return bin;
    }

    /* ---- Find/open by regex (name/date) ---- */
    public java.util.List<Path> findByRegex(String regex) throws IOException {
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
