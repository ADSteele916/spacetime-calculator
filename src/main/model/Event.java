package model;

import org.json.JSONObject;
import persistence.Writable;

import static model.World.ALLOWED_DELTA;

// Represents an event in spacetime with time and x position defined in a certain reference frame.
public class Event implements Writable {
    private String name;
    private double time;
    private double posX;
    private ReferenceFrame frame;

    // EFFECTS: constructs a new event in reference frame frame at time t and position x.
    public Event(String name, double time, double posX, ReferenceFrame frame) {
        this.name = name;
        this.time = time;
        this.posX = posX;
        this.frame = frame;
    }

    // EFFECTS: returns the name of this event
    public String getName() {
        return name;
    }

    // EFFECTS: returns the time at which the event occurs in light seconds
    public double getTime() {
        return time;
    }

    // EFFECTS: returns the position at which the event occurs in light seconds
    public double getX() {
        return posX;
    }

    // EFFECTS: returns the reference frame in which this event is defined
    public ReferenceFrame getFrame() {
        return frame;
    }

    // EFFECTS: returns a new event that is equivalent to this event viewed from a different reference frame
    public Event lorentzTransform(ReferenceFrame frame) {
        double relativeVelocity = frame.relativeVelocityTo(this.frame);
        double gamma = ReferenceFrame.gamma(relativeVelocity);
        double transformedTime = gamma * (getTime() - getX() * relativeVelocity);
        double transformedX = gamma * (getX() - getTime() * relativeVelocity);
        return new Event(getName(), transformedTime, transformedX, frame);
    }

    // EFFECTS: returns the Lorentz invariant of two events
    public double lorentzInvariant(Event otherEvent) {
        Event transformedOtherEvent = otherEvent.lorentzTransform(getFrame());
        double deltaT = getTime() - transformedOtherEvent.getTime();
        double deltaX = getX() - transformedOtherEvent.getX();
        return Math.pow(deltaX, 2) - Math.pow(deltaT, 2);
    }

    // EFFECTS: returns true if this and event are simultaneous in frame, false otherwise
    public boolean isSimultaneous(Event event, ReferenceFrame frame) {
        Event transformedThis = this.lorentzTransform(frame);
        Event transformedOther = event.lorentzTransform(frame);
        return Math.abs(transformedOther.getTime() - transformedThis.getTime()) < ALLOWED_DELTA;
    }

    // EFFECTS: returns true if this and event occur at the same place in frame, false otherwise
    public boolean isSamePlace(Event event, ReferenceFrame frame) {
        Event transformedThis = this.lorentzTransform(frame);
        Event transformedOther = event.lorentzTransform(frame);
        return Math.abs(transformedOther.getX() - transformedThis.getX()) < ALLOWED_DELTA;
    }

    // REQUIRES: frame is reachable from this.frame
    // MODIFIES: this
    // EFFECTS: changes this event's reference frame to frame
    public void changeFrame(ReferenceFrame frame) {
        Event transformedEvent = lorentzTransform(frame);
        this.time = transformedEvent.getTime();
        this.posX = transformedEvent.getX();
        this.frame = frame;
    }

    // EFFECTS: returns a JSONObject containing this event's name, time, x position, and frame
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());
        jsonObject.put("time", this.getTime());
        jsonObject.put("x", this.getX());
        jsonObject.put("frame", this.getFrame().getName());
        return jsonObject;
    }

    // EFFECTS: returns a string comprised of this event's name, time, x position, and frame
    @Override
    public String toString() {
        return name + ", at (x, t) = (" + this.posX + ", " + this.time + "), in " + this.frame.getName() + "";
    }
}
