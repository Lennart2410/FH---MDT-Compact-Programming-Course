package HomeworkAssignment3.logging;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogFilesTest {

    LogFiles logFiles = new LogFiles(Path.of("logs"));

    @Test
    public void testPathForCreatesPath() throws Exception {
        Path path = logFiles.pathFor("Test", "Unit", LocalDate.now());
        assertTrue(path.toString().contains("Test\\Unit"));
    }

    @Test
    public void testAppendLineCreatesFile() throws Exception {
        Path path = logFiles.pathFor("Test", "Append", LocalDate.now());
        logFiles.appendLine(path, "Hello, log!");
        assertTrue(Files.exists(path));
    }

    @Test
    public void testFindLogsByRegex() throws Exception {
        List<Path> results = logFiles.findLogsByRegex(".*\\.log");
        assertNotNull(results);
    }

    @Test
    public void testSafeDeleteRethrows() {
        Path fakePath = Path.of("logs/fake.log");
        assertThrows(Exception.class, () -> logFiles.safeDelete(fakePath));
    }

    @Test
    public void testReadLogSafely() throws Exception {
        Path path = logFiles.pathFor("Test", "Read", LocalDate.now());
        logFiles.appendLine(path, "Line 1");
        List<String> lines = logFiles.readLogSafely(path);
        assertTrue(lines.contains("Line 1"));
    }
}