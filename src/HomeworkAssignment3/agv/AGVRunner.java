
package HomeworkAssignment3.agv;

import HomeworkAssignment3.general.*;
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
        new Thread(() -> {
            try {
                System.out.println("AGVRunner is running a new task with " + agvTask.getAgvId());

                String logEntry = String.format("AGV %s transporting from %s to %s",
                        agvTask.getAgvId(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());

                Path logPath = logFiles.pathFor("AGV", agvTask.getAgvId(), LocalDate.now());
                logFiles.appendLine(logPath, logEntry);

                Thread.sleep(2000); // Simulate transport

                if (agvTask.getDestinationLocation().equals("packing-station")) {
                    System.out.println("AGV " + agvTask.getAgvId() + " finished transporting. Added a new packing task.");
                    addToQueue(new PackingTask(agvTask.getOrder()));
                } else if (agvTask.getDestinationLocation().equals("loading-station")) {
                    System.out.println("AGV " + agvTask.getAgvId() + " finished transporting. Added a new loading task.");
                    addToQueue(new LoadingTask(agvTask.getOrder()));
                } else {
                    throw new NoDestinationException("Invalid destination: " + agvTask.getDestinationLocation());
                }

            } catch (Exception e) {
                System.out.println("Transport failed for AGV " + agvTask.getAgvId() + ": " + e.getMessage());
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

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // checks if an agv is currently available for the task
                AGV agv = retrievePossibleAGV();
                if (agv != null) {
                    // If agv is available
                    AgvTask task = (AgvTask) in.take();
                    agv.setBusy(true);
                    task.setAgvId(agv.getId());
                    // start the concrete process
                    process(task);
                    // when the process / thread is finished, set the agv to unassigned.
                    agv.setBusy(false);
                } else {
                    System.out.println("No available picker. Task will wait.");
                }
            }
        } catch (InterruptedException stop) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // As a safety net, if a station throws, convert task to EXCEPTION
            e.printStackTrace();
        }
    }

    public AGV retrievePossibleAGV() {
        Optional<AGV> availableAgv = agvFleet.stream()
                .filter(agv -> !agv.isBusy())
                .findFirst();

        if (availableAgv.isEmpty()) {
            System.out.println("No available AGV. Task will wait.");
            return null;
        }
        return availableAgv.get();
    }
}
