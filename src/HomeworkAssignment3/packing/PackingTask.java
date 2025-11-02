package HomeworkAssignment3.packing;


import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Task;

import java.nio.file.Path;

public class PackingTask extends Task {
    private final BoxingService boxing;
    private final Path packBase;
    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the
    // generic Task

    public PackingTask(Order order, BoxingService boxing, Path logsRoot) {
        super(order);
        this.boxing = boxing;
        this.packBase = logsRoot.resolve("packing");
    }

    public BoxingService getBoxing() {
        return boxing;
    }

    public Path getPackBase() {
        return packBase;
    }

}
