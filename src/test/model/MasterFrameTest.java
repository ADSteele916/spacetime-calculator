package model;

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
        masterFrame.boost("rf1", 0.2);
        assertEquals(1, masterFrame.getRelativeFrames().size());
    }

    @Test
    void testGetRelativeFramesMany() {
        masterFrame.boost("rf1", 0.2);
        masterFrame.boost("rf2", -0.9);
        masterFrame.boost("rf3", 0.64);
        assertEquals(3, masterFrame.getRelativeFrames().size());
    }

    @Test
    void testGetVelocity() {
        assertEquals(0, masterFrame.getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testBoost() {
        RelativeFrame boostedFrame = masterFrame.boost("bf", 0.4);
        assertEquals(1, masterFrame.getRelativeFrames().size());
        assertEquals(0.4, boostedFrame.getVelocity());
        assertEquals(masterFrame, boostedFrame.getMasterFrame());
    }

    @Test
    void testGetRelativeVelocityToThis() {
        assertEquals(0, masterFrame.relativeVelocityTo(masterFrame), ALLOWED_DELTA);
    }

    @Test
    void testGetRelativeVelocityToOther() {
        RelativeFrame boostedFrame = masterFrame.boost("bf", 0.39);
        assertEquals(-0.39, masterFrame.relativeVelocityTo(boostedFrame), ALLOWED_DELTA);
    }
}
