package HomeworkAssignment1.loading;

import HomeworkAssignment1.general.Order;
import HomeworkAssignment1.general.Station;

public class LoadingStation extends Station<LoadingTask> {

    @Override
    public Order process(LoadingTask loadingTask) {
        // Loading items from the order
        // Do something that the Loading-process would usually cover
        // Process should take some seconds to make it realistic

        System.out.println("Im im loading!");

        return loadingTask.getOrder();
    }
}
