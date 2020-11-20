package model;

import model.exceptions.FasterThanLightException;
import model.exceptions.NameInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static model.World.ALLOWED_DELTA;

class MasterFrameTest {
    private MasterFrame masterFrame;

    @BeforeEach
    void setUp() {
        masterFrame = new MasterFrame();
    }

    @Test
    void testGetName() {
        assertEquals("Stationary Frame", masterFrame.getName());
    }

    @Test
    void testGetRelativeFramesNone() {
        assertEquals(0, masterFrame.getRelativeFrames().size());
    }

    @Test
    void testGetRelativeFramesOne() {
        try {
            masterFrame.boost("rf1", 0.2);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, masterFrame.getRelativeFrames().size());
    }

    @Test
    void testGetRelativeFramesMany() {
        try {
            masterFrame.boost("rf1", 0.2);
            masterFrame.boost("rf2", -0.9);
            masterFrame.boost("rf3", 0.64);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(3, masterFrame.getRelativeFrames().size());
    }

    @Test
    void testRemoveRelativeFrame() {
        RelativeFrame frame1 = null;
        RelativeFrame frame2 = null;
        try {
            frame1 = masterFrame.boost("rf1", 0.2);
            frame2 = masterFrame.boost("rf2", -0.5);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(2, masterFrame.getRelativeFrames().size());
        masterFrame.removeRelativeFrame(frame2);
        assertEquals(1, masterFrame.getRelativeFrames().size());
        assertEquals(frame1, masterFrame.getRelativeFrames().get(0));
        masterFrame.removeRelativeFrame(frame2);
        assertEquals(1, masterFrame.getRelativeFrames().size());
        masterFrame.removeRelativeFrame(frame1);
        assertEquals(0, masterFrame.getRelativeFrames().size());

    }

    @Test
    void testGetVelocity() {
        assertEquals(0, masterFrame.getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testBoostSuccess() {
        RelativeFrame boostedFrame = null;
        try {
            boostedFrame = masterFrame.boost("bf", 0.4);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(1, masterFrame.getRelativeFrames().size());
        assertEquals(0.4, boostedFrame.getVelocity());
        assertEquals(masterFrame, boostedFrame.getMasterFrame());
    }

    @Test
    void testBoostFailNameInUse() {
        try {
            masterFrame.boost("bf", 0.4);
            masterFrame.boost("bf", 0.4);
            fail();
        } catch (NameInUseException e) {
            // pass
        } catch (FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testBoostFailFasterThanLight() {
        try {
            masterFrame.boost("bf", 1.4);
            fail();
        } catch (NameInUseException e) {
            e.printStackTrace();
            fail();
        } catch (FasterThanLightException e) {
            // pass
        }
    }

    @Test
    void testGetRelativeVelocityToThis() {
        assertEquals(0, masterFrame.relativeVelocityTo(masterFrame), ALLOWED_DELTA);
    }

    @Test
    void testGetRelativeVelocityToOther() {
        RelativeFrame boostedFrame = null;
        try {
            boostedFrame = masterFrame.boost("bf", 0.39);
        } catch (NameInUseException | FasterThanLightException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(-0.39, masterFrame.relativeVelocityTo(boostedFrame), ALLOWED_DELTA);
    }

    @Test
    void testToString() {
        assertEquals("Stationary Frame", masterFrame.toString());
    }
}
