package HomeworkAssignment1.agv;

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

}
