package HomeworkAssignment1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        HomeworkAssignment1.agv.AgvTaskTest.class,
        HomeworkAssignment1.agv.AGVRunnerTest.class,
        HomeworkAssignment1.logging.LogFilesTest.class
})
public class AllTestsSuite {

}
