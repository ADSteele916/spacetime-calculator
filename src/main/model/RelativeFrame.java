package model;

import model.exceptions.FasterThanLightException;
import model.exceptions.NameInUseException;
import org.json.JSONObject;
import persistence.Writable;

// Represents a reference frame defined relative to a master frame.
public class RelativeFrame extends ReferenceFrame implements Writable {
    private MasterFrame masterFrame;

    public RelativeFrame(String name, double relativeVelocity, MasterFrame masterFrame) {
        super(name, relativeVelocity);
        this.masterFrame = masterFrame;
    }

    // EFFECTS: returns the master frame that this frame is defined relative to
    public MasterFrame getMasterFrame() {
        return masterFrame;
    }

    @Override
    public RelativeFrame boost(String name, double v) throws NameInUseException, FasterThanLightException {
        if (getMasterFrame().getFrameNames().contains(name)) {
            throw new NameInUseException();
        }
        if (Math.abs(v) >= 1) {
            throw new FasterThanLightException();
        }

        double addedVelocity = (getVelocity() + v) / (1 +  getVelocity() * v);
        RelativeFrame frame = new RelativeFrame(name, addedVelocity, getMasterFrame());
        getMasterFrame().addRelativeFrame(frame);
        return frame;
    }

    @Override
    public double relativeVelocityTo(ReferenceFrame frame) {
        return (this.getVelocity() - frame.getVelocity()) / (1 - this.getVelocity() * frame.getVelocity());
    }

    // EFFECTS: returns a JSONObject containing this event's name and velocity
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());
        jsonObject.put("velocity", this.getVelocity());
        return jsonObject;
    }
}
