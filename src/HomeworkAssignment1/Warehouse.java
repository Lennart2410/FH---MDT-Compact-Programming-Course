package HomeworkAssignment1;


import HomeworkAssignment1.agv.AGVRunner;
import HomeworkAssignment1.agv.AgvTask;
import HomeworkAssignment1.general.Item;
import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.loading.LoadingStation;
import HomeworkAssignment1.loading.LoadingTask;
import HomeworkAssignment1.packing.PackingStation;
import HomeworkAssignment1.packing.PackingTask;
import HomeworkAssignment1.picking.PickingStation;
import HomeworkAssignment1.picking.PickingTask;

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
