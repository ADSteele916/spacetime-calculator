package model;

import model.exceptions.FasterThanLightException;
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
    void testGetRelativeFramesEmpty() {
        assertEquals(0, world.getRelativeFrames().size());
    }

    @Test
    void testGetRelativeFramesOne() {
        try {
            world.getMasterFrame().boost("rf1", 0.5);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, world.getRelativeFrames().size());
        assertEquals(0.5, world.getRelativeFrames().get(0).getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testGetRelativeFramesMany() {
        try {
            world.getMasterFrame().boost("rf1", 0.5);
            world.getMasterFrame().boost("rf2", 0.75);
            world.getMasterFrame().boost("rf3", -0.25);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(3, world.getRelativeFrames().size());
        assertEquals(0.5, world.getRelativeFrames().get(0).getVelocity(), ALLOWED_DELTA);
        assertEquals(0.75, world.getRelativeFrames().get(1).getVelocity(), ALLOWED_DELTA);
        assertEquals(-0.25, world.getRelativeFrames().get(2).getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testGetAllFramesOne() {
        assertEquals(1, world.getAllFrames().size());
        assertEquals(world.getMasterFrame(), world.getAllFrames().get(0));
    }

    @Test
    void testGetAllFramesMany() {
        try {
            world.getMasterFrame().boost("rf1", 0.5);
            world.getMasterFrame().boost("rf2", 0.75);
            world.getMasterFrame().boost("rf3", -0.25);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(4, world.getAllFrames().size());
        assertEquals(world.getMasterFrame(), world.getAllFrames().get(0));
        assertEquals(0.5, world.getAllFrames().get(1).getVelocity(), ALLOWED_DELTA);
        assertEquals(0.75, world.getAllFrames().get(2).getVelocity(), ALLOWED_DELTA);
        assertEquals(-0.25, world.getAllFrames().get(3).getVelocity(), ALLOWED_DELTA);
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
