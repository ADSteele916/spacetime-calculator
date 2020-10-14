package model;

import java.util.ArrayList;
import java.util.List;

public class MasterFrame extends ReferenceFrame {
    private List<RelativeFrame> relativeFrames;

    // EFFECTS: creates a master frame with no child frames or events.
    public MasterFrame() {
        super("Stationary Frame", 0);
        relativeFrames = new ArrayList<>();
    }

    // EFFECTS: returns a list of frames defined relative to this frame
    public List<RelativeFrame> getRelativeFrames() {
        return new ArrayList<>(relativeFrames);
    }

    // MODIFIES: this
    // EFFECTS: adds a relative frame to the list of frames defined relative to this frame
    protected void addRelativeFrame(RelativeFrame frame) {
        relativeFrames.add(frame);
    }

    // REQUIRES: no events are defined in frame
    // MODIFIES: this
    // EFFECTS: removes a relative frame from the list of frames defined relative to this frame if it is on it
    public void removeRelativeFrame(RelativeFrame frame) {
        relativeFrames.remove(frame);
    }

    @Override
    public RelativeFrame boost(String name, double v) {
        RelativeFrame frame = new RelativeFrame(name, v, this);
        addRelativeFrame(frame);
        return frame;
    }

    @Override
    public double relativeVelocityTo(ReferenceFrame frame) {
        return - frame.getVelocity();
    }
}
