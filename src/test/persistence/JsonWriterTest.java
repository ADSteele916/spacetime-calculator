package persistence;

import model.Event;
import model.RelativeFrame;
import model.World;
import model.exceptions.NameInUseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Structure of tests from JsonSerializationDemo
public class JsonWriterTest extends JsonTest {

    World world;
    JsonWriter writer;
    JsonReader reader;

    @Test
    void testWriterInvalidFile() {
        try {
            world = new World();
            writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("An IOException should have been thrown.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorld() {
        try {
            world = new World();
            writer = new JsonWriter("./data/testWriterEmptyWorld.json");
            writer.open();
            writer.write(world);
            writer.close();

            reader = new JsonReader("./data/testWriterEmptyWorld.json");
            world = reader.read();
            assertEquals(0, world.getEvents().size());
            assertEquals(0, world.getFrames().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterWorldManyEventsFrames() {
        try {
            world = new World();
            RelativeFrame frame1 = world.getMasterFrame().boost("Frame1", 0.5);
            RelativeFrame frame2 = world.getFrames().get(0).boost("Frame2", 0.9);
            world.addEvent(new Event("Event1", 2, 3, world.getMasterFrame()));
            world.addEvent(new Event("Event2", 6, -2, frame1));
            world.addEvent(new Event("Event3", 9, 0, frame2));
            writer = new JsonWriter("./data/testWriterWorldManyEventsFrames.json");
            writer.open();
            writer.write(world);
            writer.close();

            reader = new JsonReader("./data/testWriterWorldManyEventsFrames.json");
            world = reader.read();
            assertEquals(2, world.getFrames().size());
            assertEquals(3, world.getEvents().size());
            checkFrame("Frame1", 0.5, world.getFrames().get(0));
            checkFrame("Frame2", 0.9655172413793103, world.getFrames().get(1));
            checkEvent("Event1", 2, 3, world.getEvents().get(0));
            checkEvent("Event2", 6, -2, world.getEvents().get(1));
            checkEvent("Event3", 9, 0, world.getEvents().get(2));
        } catch (IOException | NameInUseException e) {
            fail("Exception should not have been thrown");
        }
    }
}
