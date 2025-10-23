package CapstoneProject;

import CapstoneProject.agv.AgvTaskManager;
import CapstoneProject.shelve.PickingTask;
import CapstoneProject.shelve.PickingTaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Warehouse {

    PickingTaskManager pickingTaskManager = new PickingTaskManager(this);
    AgvTaskManager agvTaskManager = new AgvTaskManager(this);

    public Warehouse() {
        ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(1);
        executor1.scheduleAtFixedRate(
                pickingTaskManager,
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


        Item item1 = new Item("Phone");
        Item item2 = new Item("Book");
        Item item3 = new Item("Another Book");
        Item item4 = new Item("Pen");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);

        Order order1 = new Order("Example Street 1", itemList);

        pickingTaskManager.putItemIntoQueue(new PickingTask(12, 5000, order1));


//        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 5000));
//        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 10000));
//        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 4000));
//        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 3000));
//        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 12000));
//        shelveTaskManager.putItemIntoQueue(new ShelveTask(12, 15000));

    }

    public PickingTaskManager getShelveTaskManager() {
        return pickingTaskManager;
    }

    public AgvTaskManager getAgvTaskManager() {
        return agvTaskManager;
    }


}
