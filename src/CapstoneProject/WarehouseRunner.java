package CapstoneProject;


import CapstoneProject.StationsTaskManager.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WarehouseRunner {
    public static void main(String[] args) {
       // new Warehouse();
        runOrder();
    }

    private static void runOrder() {


        // ---------------- Queues (bounded) ----------------
        // One IN and OUT per station; AGVs move between OUT → next IN
        BlockingQueue<Task> qOrderToPick = new ArrayBlockingQueue<>(2); // Picking IN
        BlockingQueue<Task> qAfterPick = new ArrayBlockingQueue<>(2); // Picking OUT

        BlockingQueue<Task> qBeforePack = new ArrayBlockingQueue<>(2); // Packing IN
        BlockingQueue<Task> qAfterPack = new ArrayBlockingQueue<>(2); // Packing OUT

        BlockingQueue<Task> qBeforeLoad = new ArrayBlockingQueue<>(2); // Loading IN


        // ----------------  Logger ----------------
        //CsvLogger log = new CsvLogger();

        // ----------------  Stations ----------------
        IStation picking = new PickingStation("PICK", qOrderToPick, qAfterPick) {
            @Override
            protected Task process(Task task) throws Exception {
                System.out.println("Inventory : Reserve+Pick");
                return super.process(task).withStatus(Status.PACKING);
            }
        };

        IStation packing = new PackingStation("PACK", qBeforePack, qAfterPack) {
            @Override
            protected Task process(Task task) throws Exception {
                Task next = super.process(task); // payload = List<Parcel>, stage LOADING
                System.out.println("Packing : Cartonize");
                return next;
            }
        };

//        IStation loading = new LoadingStation("LOAD", qBeforeLoad, null, vehicles, A100.getDestination()) {
//            @Override protected Task process(Task task) throws Exception {
//                Task out = super.process(task); // may throw NoVehicleCapacityException
//                System.out.println("Vehicle : Load");
//                return out.withStatus(Status.LOADED);
//            }
//        };

        // ---------------- AGV bridges (Pick->Pack, Pack->Load) ----------------

        Runnable agvPickToPack = new AGVRunner(
                qAfterPick,     // from Picking OUT
                qBeforePack,    // to   Packing IN
                2500           // travelMs

        );

        Runnable agvPackToLoad = new AGVRunner(
                qAfterPack,     // from Packing OUT
                qBeforeLoad,    // to   Loading IN
                2500

        );

        // ---------------- 6) Start threads ----------------
        ExecutorService exec = Executors.newFixedThreadPool(5);
        exec.submit(picking);
        exec.submit(packing);
        //  exec.submit(loading);
        exec.submit(agvPickToPack);
        exec.submit(agvPackToLoad);
        //  enqueue a Task to start the flow ***
        try {
            qOrderToPick.put(new Task("A100", "A100",Status.ORDERED, null)); // like GUI creating an order
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Give it time to run so you can see prints
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        exec.shutdownNow();
    }
}


