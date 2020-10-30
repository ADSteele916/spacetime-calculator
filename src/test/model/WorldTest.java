package model;

import model.exceptions.NameInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static model.World.ALLOWED_DELTA;

class WorldTest {
    private World world;

    @BeforeEach
    void setUp() {
        world = new World();
    }

    @Test
    void testGetFramesEmpty() {
        assertEquals(0, world.getFrames().size());
    }

    @Test
    void testGetFramesOne() {
        try {
            world.getMasterFrame().boost("rf1", 0.5);
        } catch (NameInUseException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, world.getFrames().size());
        assertEquals(0.5, world.getFrames().get(0).getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testGetFramesMany() {
        try {
            world.getMasterFrame().boost("rf1", 0.5);
            world.getMasterFrame().boost("rf2", 0.75);
            world.getMasterFrame().boost("rf3", -0.25);
        } catch (NameInUseException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(3, world.getFrames().size());
        assertEquals(0.5, world.getFrames().get(0).getVelocity(), ALLOWED_DELTA);
        assertEquals(0.75, world.getFrames().get(1).getVelocity(), ALLOWED_DELTA);
        assertEquals(-0.25, world.getFrames().get(2).getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testGetEventsEmpty() {
        assertEquals(0, world.getEvents().size());
    }

    @Test
    void testGetEventsOne() {
        Event event1 = new Event("event1", 2, 4, world.getMasterFrame());
        world.addEvent(event1);
        assertEquals(1, world.getEvents().size());
        assertEquals(event1, world.getEvents().get(0));
    }

    @Test
    void testGetEventsMany() {
        Event event1 = new Event("event1", 2, 4, world.getMasterFrame());
        Event event2 = new Event("event2", 22, -4, world.getMasterFrame());
        Event event3 = new Event("event3", 1, 15, world.getMasterFrame());
        world.addEvent(event1);
        world.addEvent(event2);
        world.addEvent(event3);
        assertEquals(3, world.getEvents().size());
        assertEquals(event1, world.getEvents().get(0));
        assertEquals(event2, world.getEvents().get(1));
        assertEquals(event3, world.getEvents().get(2));
        world.removeEvent(event3);
        assertEquals(2, world.getEvents().size());

    }
}
