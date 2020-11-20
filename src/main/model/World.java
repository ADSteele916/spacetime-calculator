package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a world containing arbitrary events and reference frames.
public class World implements Writable {

    public static final double ALLOWED_DELTA = 0.000001;

    private MasterFrame masterFrame;
    private List<Event> events;

    // EFFECTS: constructs a new world with a master reference frame and no events
    public World() {
        masterFrame = new MasterFrame();
        events = new ArrayList<>();
    }

    // EFFECTS: returns the master reference frame of this world
    public MasterFrame getMasterFrame() {
        return masterFrame;
    }

    // EFFECTS: returns a list of all of the reference frames accessible from the world's master frame
    public List<RelativeFrame> getRelativeFrames() {
        return masterFrame.getRelativeFrames();
    }

    // EFFECTS: returns the world's master frame and all its relative frames in a list
    public List<ReferenceFrame> getAllFrames() {
        List<ReferenceFrame> frames = new ArrayList<>();
        frames.add(getMasterFrame());
        frames.addAll(getRelativeFrames());
        return frames;
    }

    // EFFECTS: returns a list of all of the events in the world
    public List<Event> getEvents() {
        return new ArrayList<>(this.events);
    }

    // REQUIRES: event is defined in masterFrame or a RelativeFrame whose parent is masterFrame
    // EFFECTS: adds an event to the world
    public void addEvent(Event event) {
        events.add(event);
    }

    // EFFECTS: removes an event from the world
    public void removeEvent(Event event) {
        events.remove(event);
    }

    // EFFECTS: creates a JSONObject containing the world's relative frames and events.
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("frames", this.framesToJson());
        jsonObject.put("events", this.eventsToJson());
        return jsonObject;
    }

    // EFFECTS: returns a JSONArray of JSON objects for the world's reference frames
    private JSONArray framesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (RelativeFrame frame: this.getRelativeFrames()) {
            jsonArray.put(frame.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns a JSONArray of JSON objects for the world's events
    private JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Event event: this.getEvents()) {
            jsonArray.put(event.toJson());
        }
        return jsonArray;
    }
}
