package model;

import model.exceptions.NameInUseException;
import org.json.JSONObject;
import persistence.Writable;

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
    public RelativeFrame boost(String name, double v) throws NameInUseException {
        if (getMasterFrame().getFrameNames().contains(name)) {
            throw new NameInUseException();
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

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.getName());
        jsonObject.put("velocity", this.getVelocity());
        return jsonObject;
    }
}
