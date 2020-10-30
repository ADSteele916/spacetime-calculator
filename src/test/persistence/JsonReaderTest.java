package persistence;

import model.World;
import persistence.exceptions.InvalidDataException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Structure of tests from JsonSerializationDemo
public class JsonReaderTest extends JsonTest {

    World world;

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            world = reader.read();
            fail("An IOException should have been thrown.");
        } catch (InvalidDataException e) {
            e.printStackTrace();
            fail("An IOException should have been thrown, not an InvalidDataException.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderInvalidFrame() {
        JsonReader reader = new JsonReader("./data/testReaderInvalidFrame.json");
        try {
            world = reader.read();
            fail("An InvalidDataException should have been thrown.");
        } catch (InvalidDataException e) {
            // pass
        } catch (IOException e) {
            e.printStackTrace();
            fail("An InvalidDataException should have been thrown, not an IOException.");
        }
    }

    @Test
    void testReaderDuplicateFrame() {
        JsonReader reader = new JsonReader("./data/testReaderDuplicateFrame.json");
        try {
            world = reader.read();
            fail("An InvalidDataException should have been thrown.");
        } catch (InvalidDataException e) {
            // pass
        } catch (IOException e) {
            e.printStackTrace();
            fail("An InvalidDataException should have been thrown, not an IOException.");
        }
    }

    @Test
    void testReaderNoFrames() {
        JsonReader reader = new JsonReader("./data/testReaderNoFrames.json");
        try {
            world = reader.read();
            fail("An InvalidDataException should have been thrown.");
        } catch (InvalidDataException e) {
            // pass
        } catch (IOException e) {
            e.printStackTrace();
            fail("An InvalidDataException should have been thrown, not an IOException.");
        }
    }

    @Test
    void testReaderEmptyWorld() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorld.json");
        try {
            world = reader.read();
            assertEquals(0, world.getFrames().size());
            assertEquals(0, world.getEvents().size());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't read from file.");
        } catch (InvalidDataException e) {
            e.printStackTrace();
            fail("The file is invalid.");
        }
    }

    @Test
    void testReaderWorldManyEventsFrames() {
        JsonReader reader = new JsonReader("./data/testReaderWorldManyEventsFrames.json");
        try {
            world = reader.read();
            checkFrame("Frame1", 0.5, world.getFrames().get(0));
            checkFrame("Frame2", 0.9655172413793103, world.getFrames().get(1));
            checkEvent("Event1", 2, 3, world.getEvents().get(0));
            assertEquals("Stationary Frame", world.getEvents().get(0).getFrame().getName());
            checkEvent("Event2", 6, -2, world.getEvents().get(1));
            assertEquals("Frame1", world.getEvents().get(1).getFrame().getName());
            checkEvent("Event3", 9, 0, world.getEvents().get(2));
            assertEquals("Frame2", world.getEvents().get(2).getFrame().getName());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't read from file.");
        } catch (InvalidDataException e) {
            e.printStackTrace();
            fail("The file is invalid.");
        }
    }
}
