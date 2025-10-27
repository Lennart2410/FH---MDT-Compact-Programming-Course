package HomeworkAssignment1.packing;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Task;
import HomeworkAssignment1.logging.LogFiles;

import java.nio.file.Path;

public class PackingTask extends Task {
    private final BoxingService boxing;
    private final LogFiles storage;
    private final Path packBase;
    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the generic Task

    public PackingTask(Order order, BoxingService boxing, LogFiles storage , Path logsRoot) {
        super(order);
        this.boxing = boxing;
        this.storage = storage;
        this.packBase = logsRoot.resolve("packing");
    }

    public BoxingService getBoxing() {
        return boxing;
    }

    public Path getPackBase() {
        return packBase;
    }
    public LogFiles getStorage() {
        return storage;
    }

}
