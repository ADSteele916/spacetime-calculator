package model;

public class RelativeFrame extends ReferenceFrame {
    private MasterFrame masterFrame;

    public RelativeFrame(String name, double relativeVelocity, MasterFrame masterFrame) {
        super(name, relativeVelocity);
        this.masterFrame = masterFrame;
    }

    public MasterFrame getMasterFrame() {
        return masterFrame;
    }

    @Override
    public RelativeFrame boost(String name, double v) {
        double addedVelocity = (getVelocity() + v) / (1 +  getVelocity() * v);
        RelativeFrame frame = new RelativeFrame(name, addedVelocity, getMasterFrame());
        getMasterFrame().addRelativeFrame(frame);
        return frame;
    }

    @Override
    public double relativeVelocityTo(ReferenceFrame frame) {
        return (this.getVelocity() - frame.getVelocity()) / (1 - this.getVelocity() * frame.getVelocity());
    }
}
