package system;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class StorageVehicle {
    private String id;

    public StorageVehicle(String id) {
        this.id = id;
    }

    public void logActivity(String message) {
        String filename = "logs/" + id + "_" + LocalDate.now() + ".log";
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing log: " + e.getMessage());
        }
    }
}