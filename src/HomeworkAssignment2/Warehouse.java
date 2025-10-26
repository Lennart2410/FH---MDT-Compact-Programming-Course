package HomeworkAssignment2;


import HomeworkAssignment2.agv.AGVRunner;
import HomeworkAssignment2.agv.AgvTask;
import HomeworkAssignment2.general.Item;
import HomeworkAssignment2.general.Order;
import HomeworkAssignment2.loading.LoadingStation;
import HomeworkAssignment2.loading.LoadingTask;
import HomeworkAssignment2.packing.PackingStation;
import HomeworkAssignment2.packing.PackingTask;
import HomeworkAssignment2.picking.PickingStation;
import HomeworkAssignment2.picking.PickingTask;

import java.util.List;


public class Warehouse {
    PickingStation pickingStation = new PickingStation();
    PackingStation packingStation  = new PackingStation();
    AGVRunner agvRunner = new AGVRunner();
    LoadingStation loadingStation  = new LoadingStation();

    public Warehouse() {
        Item item1 = new Item("Phone");
        Item item2 = new Item("Book");
        Item item3 = new Item("Another Book");
        Item item4 = new Item("Pen");

        List<Item> itemList = List.of(item1, item2, item3, item4);

        processOrder(new Order("Example Street 1", itemList));

    }

    public void processOrder(Order order){
        // Picking process from shelves
        order = pickingStation.process(new PickingTask(order));

        // Bringing items from the order to the packaging
        order = agvRunner.process(new AgvTask(order,"shelves","packing-station"));

        // Packaging the items inside the order
        order = packingStation.process(new PackingTask(order));

        // Bringing items from the packaging to the loading
        order = agvRunner.process(new AgvTask(order,"packing-station","loading-station"));

        // Loading items into the delivery vehicle
        loadingStation.process(new LoadingTask(order));
    }

}
