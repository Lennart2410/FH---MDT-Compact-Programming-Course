/*package HomeworkAssignment1.agv;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Station;

public class AGVRunner extends Station<AgvTask> {

    @Override
    public Order process(AgvTask agvTask) {
        // Maybe transform the string from start and destinations into coordinates with x/y or something similiar.
        // Traveling with the order from startingLocation to destinationLocation
        // Do something that the transporting-process would usually cover
        // Process should take some seconds to make it realistic

        System.out.println("Im im transporting!");

        return agvTask.getOrder();
    }

}*/

package HomeworkAssignment2.agv;

import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.general.Station;
import HomeworkAssignment2.logging.LogFiles;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;

public class AGVRunner extends Station<AgvTask> {
    private final LogFiles logFiles;

    public AGVRunner(Path logBaseDir) {
        this.logFiles = new LogFiles(logBaseDir);
    }

    @Override
    public Order process(AgvTask agvTask) {
        try {
            String logEntry = String.format("AGV %s transporting from %s to %s",
                    agvTask.getAgvId(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());

            Path logPath = logFiles.pathFor("AGV", agvTask.getAgvId(), LocalDate.now());
            logFiles.appendLine(logPath, logEntry);

            System.out.println(logEntry);
            Thread.sleep(2000); // Simulate transport time

            return agvTask.getOrder();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("AGV transport interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("Transport failed for AGV " + agvTask.getAgvId(), e);
        }
    }

    // HA1: Byte stream simulation for AGV task exchange
    public void simulateByteStream(AgvTask task) {
        try (var out = new ObjectOutputStream(new FileOutputStream("agvData.bin"));
                var in = new ObjectInputStream(new FileInputStream("agvData.bin"))) {

            out.writeObject(task); // Serialize
            AgvTask received = (AgvTask) in.readObject(); // Deserialize

            System.out.println("Received task: " + received.getStartingLocation() +
                    " → " + received.getDestinationLocation());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Stream simulation failed", e);
        }
    }

    // HA2.4: Chaining Exceptions
    public void simulateFailure() {
        try {
            throw new IOException("Disk error");
        } catch (IOException e) {
            throw new RuntimeException("Transport failed", e); // Chained exception
        }
    }

    // Optional: expose logFiles for archiving or testing
    public LogFiles getLogFiles() {
        return logFiles;
    }
}