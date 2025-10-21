package CapstoneProject;

import CapstoneProject.agv.AgvTaskManager;
import CapstoneProject.shelve.ShelveTask;
import CapstoneProject.shelve.ShelveTaskManager;

import java.util.concurrent.*;

public class Warehouse {

    ShelveTaskManager shelveTaskManager = new ShelveTaskManager(this);
    AgvTaskManager agvTaskManager = new AgvTaskManager(this);

    public Warehouse() {
        ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(1);
        executor1.scheduleAtFixedRate(
                shelveTaskManager,
                0, // Initial delay
                2, // polling interval
                TimeUnit.SECONDS
        );

        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(
                agvTaskManager,
                0, // Initial delay
                2, // polling interval
                TimeUnit.SECONDS
        );


        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 5000));
        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 10000));
        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 4000));
        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 3000));
        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 12000));
        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 15000));
    }

    public ShelveTaskManager getShelveTaskManager() {
        return shelveTaskManager;
    }

    public AgvTaskManager getAgvTaskManager() {
        return agvTaskManager;
    }


}
