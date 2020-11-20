package model;

import model.exceptions.FasterThanLightException;
import model.exceptions.NameInUseException;

// Represents a reference frame moving at some velocity
public abstract class ReferenceFrame {
    private String name;
    private double relativeVelocity;

    // EFFECTS: constructs the reference frame
    public ReferenceFrame(String name, double relativeVelocity) {
        this.name = name;
        this.relativeVelocity = relativeVelocity;
    }

    // EFFECTS: returns the name of this frame
    public String getName() {
        return name;
    }

    // EFFECTS: returns the velocity of a frame relative to its master frame, 0 if this is a master frame
    public double getVelocity() {
        return relativeVelocity;
    }

    // EFFECTS: returns a new relative frame with given name boosted from this at velocity v*c
    //          throws NameInUseException if the given name is already being used
    //          throws FasterThanLightException if the desired speed is greater than c.
    public abstract RelativeFrame boost(String name, double v) throws NameInUseException, FasterThanLightException;

    // REQUIRES: frame is this or has same parent as this
    // EFFECTS: returns the velocity of this frame relative to given frame
    public abstract double relativeVelocityTo(ReferenceFrame frame);

    // REQUIRES: -1 < v < 1
    // EFFECTS: returns the Lorentz factor for a given velocity.
    public static double gamma(double v) {
        return 1 / Math.sqrt(1 - Math.pow(v, 2));
    }

    // EFFECTS: returns the name of this frame
    @Override
    public String toString() {
        return name;
    }
}
