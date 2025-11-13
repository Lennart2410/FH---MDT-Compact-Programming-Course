package CapstoneProject.packing;


import CapstoneProject.general.Order;
import CapstoneProject.general.Task;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PackingTask extends Task {
    private final BoxingService boxing;
    private final Path packBase;
    private  String packerID;
    // Specific content of the PackingTask
    // Should contain attributes which only accounts to the PackingTask and not the
    // generic Task

    public PackingTask(Order order) {
        super(order);
        this.boxing = new OrderBoxingService(order);
        this.packBase = Paths.get("logs");
    }

    public BoxingService getBoxing() {
        return boxing;
    }

    public Path getPackBase() {
        return packBase;
    }

    public String getPackerID() {
        return packerID;
    }

    public void setPackerID(String packerID) {
        this.packerID = packerID;
    }
}
