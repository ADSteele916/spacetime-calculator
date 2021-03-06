package persistence;

import model.Event;
import model.ReferenceFrame;
import model.RelativeFrame;
import model.World;
import model.exceptions.FasterThanLightException;
import persistence.exceptions.InvalidDataException;
import model.exceptions.NameInUseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Structure of constructor, read, and readFile from JsonSerializationDemo
public class JsonReader {
    private String source;

    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads world from file and returns it;
    // throws IOException if an error occurs reading data from file
    public World read() throws IOException, InvalidDataException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorld(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses world from JSON object and returns it
    private World parseWorld(JSONObject jsonObject) throws InvalidDataException {
        World world = new World();
        addFrames(world, jsonObject);
        addEvents(world, jsonObject);
        return world;
    }

    // MODIFIES: world
    // EFFECTS: adds the frames contained in the file to the world
    private void addFrames(World world, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("frames");
        for (Object json : jsonArray) {
            JSONObject nextFrame = (JSONObject) json;
            addFrame(world, nextFrame);
        }
    }

    // MODIFIES: world
    // EFFECTS: adds the frame contained in the file to the world
    //          throws InvalidDataException if a frame already has the required name
    private void addFrame(World world, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double velocity = jsonObject.getDouble("velocity");
        try {
            world.getMasterFrame().boost(name, velocity);
        } catch (NameInUseException | FasterThanLightException e) {
            throw new InvalidDataException();
        }
    }

    // MODIFIES: world
    // EFFECTS: adds the events contained in the file to the world
    private void addEvents(World world, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("events");
        for (Object json : jsonArray) {
            JSONObject nextFrame = (JSONObject) json;
            addEvent(world, nextFrame);
        }
    }

    // MODIFIES: world
    // EFFECTS: adds the event contained in the file to the world
    //          throws InvalidDataException if the frame in which the event is defined cannot be found
    private void addEvent(World world, JSONObject jsonObject) throws InvalidDataException {
        String name = jsonObject.getString("name");
        double time = jsonObject.getDouble("time");
        double x = jsonObject.getDouble("x");
        String frameName = jsonObject.getString("frame");
        ReferenceFrame frame = null;
        if (frameName.equals("Stationary Frame")) {
            frame = world.getMasterFrame();
        } else {
            for (RelativeFrame relativeFrame : world.getRelativeFrames()) {
                if (relativeFrame.getName().equals(frameName)) {
                    frame = relativeFrame;
                    break;
                }
            }
        }
        if (frame == null) {
            throw new InvalidDataException("There is no frame with name " + frameName);
        }
        Event event = new Event(name, time, x, frame);
        world.addEvent(event);
    }

}
