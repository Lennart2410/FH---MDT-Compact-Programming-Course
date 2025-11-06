package HomeworkAssignment3.agv;

import HomeworkAssignment3.agv.energyloading.AGVEnergyStation;
import HomeworkAssignment3.agv.energyloading.LoadingSlot;
import HomeworkAssignment3.agv.exceptions.AGVException;
import HomeworkAssignment3.agv.exceptions.EnergyStationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AGVEnergyLoadingStationTest {

    private AGV agv01;
    private AGV agv02;
    private AGVRunner agvRunner;

    @BeforeEach
    public void setUp() throws AGVException {
        agvRunner = new AGVRunner(null, null, null);
        agvRunner.getAgvEnergyStation().addLoadingSlot(new LoadingSlot());
        agv01 = new AGV("agv01");
        agv02 = new AGV("agv02");

        agvRunner.getAgvFleet().add(agv01);
        agvRunner.getAgvFleet().add(agv02);


    }

    @Test
    public void agvLoadingTest() throws InterruptedException {
        agv01.setEnergyLevel(10);
        agv02.setEnergyLevel(99);

        long freeLoadingslots = agvRunner.getAgvEnergyStation().getLoadingSlotList().stream().filter(LoadingSlot::isOccupation).count();

        Assertions.assertEquals(0, freeLoadingslots);

        agvRunner.startEnergyLoadingTask(agv01);
        agvRunner.startEnergyLoadingTask(agv02);

        // The values still should be the same, because the thread is still not finished
        Assertions.assertEquals(10, agv01.getEnergyLevel());
        Assertions.assertEquals(99, agv02.getEnergyLevel());
    }
}
