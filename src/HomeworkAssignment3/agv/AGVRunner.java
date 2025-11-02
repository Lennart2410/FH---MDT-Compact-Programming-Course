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
import java.util.concurrent.BlockingQueue;

public class AGVRunner extends Station<AgvTask> {
    private final LogFiles logFiles;

    public AGVRunner(Path logBaseDir, BlockingQueue<Task> in, BlockingQueue<Task> out) {
        super(in,out);
        this.logFiles = new LogFiles(logBaseDir);
    }

    @Override
    public void process(AgvTask agvTask) throws WarehouseException {
        System.out.println("AGVRunner is running a new task.");
        try {
            String logEntry = String.format("AGV %s transporting from %s to %s",
                    agvTask.getAgvId(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());

            Path logPath = logFiles.pathFor("AGV", agvTask.getAgvId(), LocalDate.now());
            logFiles.appendLine(logPath, logEntry);


            Thread.sleep(2000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("AGV transport interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("Transport failed for AGV " + agvTask.getAgvId(), e);
        }
        try {
            if (agvTask.getDestinationLocation().equals("packing-station")) {
                System.out.println("AGVRunner is finished transporting. Added a new packing task.");
                addToQueue(new PackingTask(agvTask.getOrder()));
            } else if (agvTask.getDestinationLocation().equals("loading-station")) {
                System.out.println("AGVRunner is finished transporting. Added a new loading task.");
                addToQueue(new LoadingTask(agvTask.getOrder()));
            } else {
                // No correct destination was set. Throwing an exception.
                throw new NoDestinationException("The destination location of the previous task was not set. It was: " + agvTask.getDestinationLocation());
            }
        }catch (NoDestinationException e){
            throw new WarehouseException(e.getMessage(),e);
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