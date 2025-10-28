package HomeworkAssignment1.logging;

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

public class LogFiles {
    private final Path baseDir;
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogFiles(Path baseDir) { this.baseDir = baseDir; }

    public Path logSystem(String actor, String event, String detail) throws IOException {
        Path file = pathFor("system", null, LocalDate.now());
        appendLine(file, line(actor, event, detail));
        return file;
    }

    private String line(String actor, String event, String detail) {
        return String.format("%s,%s,%s,%s", TIME.format(LocalTime.now()), actor, event, detail);
    }

    private Path pathFor(String category, String name, LocalDate day) throws IOException {
        Path dir = (name == null)
                ? baseDir.resolve(category)
                : baseDir.resolve(category).resolve(name);
        Files.createDirectories(dir);
        return dir.resolve(DAY.format(day) + ".log");
    }

    private void appendLine(Path file, String text) throws IOException {
        try (var w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(text);
            w.newLine();
        }
    }

    /* ---------- 2) File mgmt: move, delete, archive (byte streams) ---------- */

    public void move(Path src, Path dst) throws IOException {
        Files.createDirectories(dst.getParent());
        Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    public void delete(Path p) throws IOException {
        Files.deleteIfExists(p);
    }


    /* ---------- 3) Find logs by regex on full path ---------- */

    public List<Path> findLogsByRegex(String regex) throws IOException {
        Pattern p = Pattern.compile(regex);
        List<Path> hits = new ArrayList<>();
        try (var walk = Files.walk(baseDir)) {
            walk.filter(Files::isRegularFile).forEach(path -> {
                if (p.matcher(path.toString()).find()) hits.add(path);
            });
        }
        return hits;
    }
}
