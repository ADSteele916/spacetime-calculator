package persistence;

import model.Event;
import model.ReferenceFrame;

import static model.World.ALLOWED_DELTA;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    protected void checkFrame(String name, double velocity, ReferenceFrame frame) {
        assertEquals(name, frame.getName());
        assertEquals(velocity, frame.getVelocity(), ALLOWED_DELTA);
    }
    protected void checkEvent(String name, double t, double x, Event event) {
        assertEquals(name, event.getName());
        assertEquals(t, event.getTime(), ALLOWED_DELTA);
        assertEquals(x, event.getX(), ALLOWED_DELTA);
    }
}
