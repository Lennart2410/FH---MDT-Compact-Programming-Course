/*package HomeworkAssignment1.logging;

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

    

    public void move(Path src, Path dst) throws IOException {
        Files.createDirectories(dst.getParent());
        Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
    }

    public void delete(Path p) throws IOException {
        Files.deleteIfExists(p);
    }


    

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
}*/

package HomeworkAssignment3.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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
    private final List<LogListener> listeners = new ArrayList<>();

    public LogFiles() {
        this.baseDir = Paths.get("logs");
    }

    public LogFiles(Path baseDir) {
        this.baseDir = baseDir;
    }

    public void addListener(LogListener listener) {
        listeners.add(listener);
    }

    // Format a log line with timestamp, actor, event, and detail
    public String line(String actor, String event, String detail) {
        addLogInUi(actor,detail);
        return String.format("%s,%s,%s,%s", TIME.format(LocalTime.now()), actor, event, detail);
    }

    // Create or get the path for a log file based on category, name, and date
    public Path pathFor(String category, String name, LocalDate day) throws IOException {
        Path dir = (name == null)
                ? baseDir.resolve(category)
                : baseDir.resolve(category).resolve(name);
        Files.createDirectories(dir);
        return dir.resolve(DAY.format(day) + ".log");
    }


    // Append a line of text to the specified log file
    public void appendLine(Path file, String text) throws IOException {
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

    // HA2.2: Re-throwing exception example
    public void safeDelete(Path p) throws IOException {
        try {
            Files.delete(p);
        } catch (IOException e) {
            System.err.println("Delete failed: " + e.getMessage());
            throw e; // Re-throwing the original exception
        }
    }

    // Archive a log file into the "archive" folder
    public void archiveLog(Path sourceLog) throws IOException {
        Path archiveDir = baseDir.resolve("archive");
        Files.createDirectories(archiveDir);
        Path archivedLog = archiveDir.resolve(sourceLog.getFileName());
        Files.move(sourceLog, archivedLog, StandardCopyOption.REPLACE_EXISTING);
    }

    /* ---------- 3) Find logs by regex on full path ---------- */

    public List<Path> findLogsByRegex(String regex) throws IOException {
        Pattern p = Pattern.compile(regex);
        List<Path> hits = new ArrayList<>();
        try (var walk = Files.walk(baseDir)) {
            walk.filter(Files::isRegularFile).forEach(path -> {
                if (p.matcher(path.toString()).find())
                    hits.add(path);
            });
        }
        return hits;
    }

    // HA1: Open logs based on name or date query
    public void openLogByNameOrDate(String query) {
        try {
            List<Path> logs = findLogsByRegex(query);
            for (Path log : logs) {
                System.out.println("Found log: " + log);
                Files.lines(log).forEach(System.out::println);
            }
        } catch (IOException e) {
            System.err.println("Error reading logs: " + e.getMessage());
        }
    }

    // HA2.3: Resource-managed log reader
    public List<String> readLogSafely(Path logFile) throws IOException {
        try (var reader = Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
            return reader.lines().toList();
        }
    }

    //----------------Implementation Lennart---------------

    public void writeLogEntry(String textToLog, String subpath) {
        try {
            Path pathToWrite = pathFor(subpath, LocalDate.now());
            appendLine(pathToWrite, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": " + textToLog);
        } catch (IOException e) {
            // Log not possible
        }
        addLogInUi(subpath,LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": " + textToLog);
    }

    public Path pathFor(String subPath, LocalDate day) throws IOException {
        Path dir = baseDir.resolve(subPath);
        Files.createDirectories(dir);
        return dir.resolve(DAY.format(day) + ".log");
    }

    public List<LogListener> getListeners() {
        return listeners;
    }

    private void addLogInUi(String station, String textToLog){
        for (LogListener l : listeners) {
            l.onLog(station, textToLog);
        }
    }
}