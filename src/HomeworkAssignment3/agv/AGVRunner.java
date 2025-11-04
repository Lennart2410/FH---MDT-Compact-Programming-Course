
package HomeworkAssignment3.agv;

import HomeworkAssignment3.general.Order;
import HomeworkAssignment3.general.Station;
import HomeworkAssignment3.general.Task;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.loading.LoadingTask;
import HomeworkAssignment3.loading.exceptions.NoDestinationException;
import HomeworkAssignment3.logging.LogFiles;
import HomeworkAssignment3.packing.PackingTask;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class AGVRunner extends Station<AgvTask> {
    private final LogFiles logFiles;
    private final List<AGV> agvFleet = new ArrayList<>();

    public AGVRunner(Path logBaseDir, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in, out);
        this.logFiles = new LogFiles(logBaseDir);
        agvFleet.add(new AGV("AGV1"));
        agvFleet.add(new AGV("AGV2"));
        agvFleet.add(new AGV("AGV3"));
    }

    @Override
    public void process(AgvTask agvTask) throws WarehouseException {
        Optional<AGV> availableAgv = agvFleet.stream()
                .filter(agv -> !agv.isBusy())
                .findFirst();

        if (availableAgv.isEmpty()) {
            System.out.println("No available AGV. Task will wait.");
            return;
        }

        AGV agv = availableAgv.get();
        agv.setBusy(true);
        agvTask.setAgvId(agv.getId());

        new Thread(() -> {
            try {
                System.out.println("AGVRunner is running a new task with " + agv.getId());

                String logEntry = String.format("AGV %s transporting from %s to %s",
                        agv.getId(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());

                Path logPath = logFiles.pathFor("AGV", agv.getId(), LocalDate.now());
                logFiles.appendLine(logPath, logEntry);

                Thread.sleep(2000); // Simulate transport

                if (agvTask.getDestinationLocation().equals("packing-station")) {
                    System.out.println("AGV " + agv.getId() + " finished transporting. Added a new packing task.");
                    addToQueue(new PackingTask(agvTask.getOrder()));
                } else if (agvTask.getDestinationLocation().equals("loading-station")) {
                    System.out.println("AGV " + agv.getId() + " finished transporting. Added a new loading task.");
                    addToQueue(new LoadingTask(agvTask.getOrder()));
                } else {
                    throw new NoDestinationException("Invalid destination: " + agvTask.getDestinationLocation());
                }

            } catch (Exception e) {
                System.out.println("Transport failed for AGV " + agv.getId() + ": " + e.getMessage());
            } finally {
                agv.setBusy(false);
            }
        }).start();
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

    public LogFiles getLogFiles() {
        return logFiles;
    }
}
