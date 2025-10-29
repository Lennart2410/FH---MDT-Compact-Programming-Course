package HomeworkAssignment1.picking;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
                PickingStationTest.class,
                ExceptionTest.class
})
public class AllTestsSuite {
}