
package HomeworkAssignment3.agv;

import HomeworkAssignment3.agv.energyloading.AGVEnergyStation;
import HomeworkAssignment3.agv.energyloading.LoadingSlot;
import HomeworkAssignment3.agv.exceptions.AGVException;
import HomeworkAssignment3.agv.exceptions.EnergyStationException;
import HomeworkAssignment3.general.*;
import HomeworkAssignment3.general.exceptions.WarehouseException;
import HomeworkAssignment3.loading.LoadingTask;
import HomeworkAssignment3.loading.exceptions.NoDestinationException;
import HomeworkAssignment3.logging.LogFiles;
import HomeworkAssignment3.packing.PackingTask;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AGVRunner extends Station<AgvTask> {
    private final LogFiles logFiles;
    private final List<AGV> agvFleet = new ArrayList<>();
    private final AGVEnergyStation agvEnergyStation = new AGVEnergyStation();

    public AGVRunner(Path logBaseDir, BlockingQueue<Task> in, BlockingQueue<Task> out, OrderStatusListener listener) throws AGVException {
        super(in, out, listener);
        this.logFiles = new LogFiles(logBaseDir);
        agvFleet.add(new AGV("AGV1"));
        agvFleet.add(new AGV("AGV2"));
        agvFleet.add(new AGV("AGV3"));

        try {
            agvEnergyStation.addLoadingSlot(new LoadingSlot());
        } catch (EnergyStationException e) {
            throw new AGVException("There was a problem in creating the AGVRunner. ", e);
        }
    }

    @Override
    public void process(AgvTask agvTask) throws WarehouseException {
        new Thread(() -> {
            try {
                logManager.writeLogEntry("A AGVTask with the order number " + agvTask.getOrder().getOrderNumber() + " should be processed.", "Warehouse");
                agvTask.getAgv().setBusy(true);
                System.out.println("AGVRunner is running a new task with " + agvTask.getAgv().getId());

                String logEntry = String.format("%s is transporting order %s from %s to %s",
                        agvTask.getAgv().getId(), agvTask.getOrder().getOrderNumber(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());

                logManager.writeLogEntry(logEntry, "AGVRunner");

                Thread.sleep(2000); // Simulate transport

                if (agvTask.getDestinationLocation().equals("packing-station")) {
                    System.out.println("AGV " + agvTask.getAgv().getId() + " finished transporting. Added a new packing task.");
                    String logEntryNewPackingTask = String.format("%s finished transporting order %s from %s to %s. Creating a new Task for the packing station.",
                            agvTask.getAgv().getId(), agvTask.getOrder().getOrderNumber(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());
                    logManager.writeLogEntry(logEntryNewPackingTask, "AGVRunner");
                    addToQueue(new PackingTask(agvTask.getOrder()));
                } else if (agvTask.getDestinationLocation().equals("loading-station")) {
                    System.out.println("AGV " + agvTask.getAgv().getId() + " finished transporting. Added a new loading task.");
                    String logEntryNewLoadingTask = String.format("%s finished transporting order %s from %s to %s. Creating a new Task for the loading station.",
                            agvTask.getAgv().getId(), agvTask.getOrder().getOrderNumber(), agvTask.getStartingLocation(), agvTask.getDestinationLocation());
                    logManager.writeLogEntry(logEntryNewLoadingTask, "AGVRunner");
                    addToQueue(new LoadingTask(agvTask.getOrder()));
                } else {
                    agvTask.getOrder().setOrderStatusEnum(OrderStatusEnum.EXCEPTION);
                    getListener().onOrderStatusChanged(agvTask.getOrder());
                    throw new NoDestinationException("Invalid destination: " + agvTask.getDestinationLocation());
                }

            } catch (Exception e) {
                System.out.println("Transport failed for AGV " + agvTask.getAgv().getId() + ": " + e.getMessage());
            } finally {
                agvTask.getAgv().setEnergyLevel(agvTask.getAgv().getEnergyLevel() - 15.0);
                agvTask.getAgv().setBusy(false);
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
                List<AGV> freeAgvList = retrievePossibleAGV();
                if (!freeAgvList.isEmpty()) {
                    AGV agv = freeAgvList.get(0);
                    // If agv is available
                    AgvTask task = (AgvTask) in.take();
                    task.setAgv(agv);
                    // start the concrete process
                    process(task);
                    // when the process / thread is finished, set the agv to unassigned.

                    freeAgvList = retrievePossibleAGV().stream().filter(freeAgv -> freeAgv.getEnergyLevel() < 20.0).toList();
                    boolean isEnergyslotAvailable = !agvEnergyStation.getLoadingSlotList().stream().filter(energyslot -> !energyslot.isOccupation()).toList().isEmpty();

                    if (in.isEmpty() && !freeAgvList.isEmpty() && isEnergyslotAvailable) {

                        //Start loading process for agv
                        startEnergyLoadingTask(freeAgvList.get(0));
                    }
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

    public List<AGV> retrievePossibleAGV() {
        return agvFleet.stream()
                .filter(agv -> !agv.isBusy()).toList();
    }

    public void startEnergyLoadingTask(AGV agv) {
        new Thread(() -> {
            agv.setBusy(true);
            LoadingSlot nextLoadingSlot;
            try {
                nextLoadingSlot = agvEnergyStation.getLoadingSlotList().stream().findFirst().orElseThrow(() -> new EnergyStationException("There was no free energy station loading slot free."));
            } catch (EnergyStationException e) {
                throw new RuntimeException(e);
            }

            double missingEnergyLevel = 100.0 - agv.getEnergyLevel();
            double minutesToReacharge = missingEnergyLevel / nextLoadingSlot.getLoadingSpeedPerMinute();
            // If recharge needs 15 or more minutes and only queue still has some tasks
            if (minutesToReacharge > 15.0 && !in.isEmpty()) {
                agv.setBusy(false);
                nextLoadingSlot.setOccupation(false);
            } else {
                nextLoadingSlot.setOccupation(true);
                try {
                    long sleepingSecondsInMS = (long) ((60 / minutesToReacharge) * 1000);
                    Thread.sleep(sleepingSecondsInMS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    agv.setBusy(false);
                    nextLoadingSlot.setOccupation(false);
                }
                // Fully recharged
                agv.setEnergyLevel(agv.getEnergyLevel() + missingEnergyLevel);
            }
        });
    }


    public List<AGV> getAgvFleet() {
        return agvFleet;
    }

    public AGVEnergyStation getAgvEnergyStation() {
        return agvEnergyStation;
    }
}
