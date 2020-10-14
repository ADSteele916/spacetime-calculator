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

    protected void addRelativeFrame(RelativeFrame frame) {
        relativeFrames.add(frame);
    }

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
