package HomeworkAssignment3.picking;

import HomeworkAssignment3.picking.exceptions.ItemNotFoundException;
import HomeworkAssignment3.picking.exceptions.PickingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    @Test
    public void testItemNotFoundMessageOnly() {
        ItemNotFoundException ex = new ItemNotFoundException("Item missing");
        assertEquals("Item missing", ex.getMessage());
    }

    @Test
    public void testItemNotFoundWithCause() {
        Exception cause = new Exception("Root cause");
        ItemNotFoundException ex = new ItemNotFoundException("Item missing", cause);
        assertEquals("Item missing", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    public void testPickingExceptionMessageOnly() {
        PickingException ex = new PickingException("Picking failed");
        assertEquals("Picking failed", ex.getMessage());
    }

    @Test
    public void testPickingExceptionWithCause() {
        Exception cause = new Exception("AGV error");
        PickingException ex = new PickingException("Picking failed", cause);
        assertEquals("Picking failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}