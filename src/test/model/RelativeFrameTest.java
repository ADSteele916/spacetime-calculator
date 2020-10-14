package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static model.World.ALLOWED_DELTA;

class RelativeFrameTest {
    private MasterFrame masterFrame;
    private RelativeFrame relativeFrame;

    @BeforeEach
    void setUp() {
        masterFrame = new MasterFrame();
        relativeFrame = masterFrame.boost("rf", 0.5);
    }

    @Test
    void testGetName() {
        assertEquals("rf", relativeFrame.getName());
    }

    @Test
    void testGetMasterFrame() {
        assertEquals(masterFrame, relativeFrame.getMasterFrame());
    }

    @Test
    void testGetVelocity() {
        assertEquals(0.5, relativeFrame.getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testBoost() {
        RelativeFrame boostedFrame = relativeFrame.boost("bf", 0.2);
        assertEquals(masterFrame, boostedFrame.getMasterFrame());
        assertEquals(7.0 / 11.0, boostedFrame.getVelocity(), ALLOWED_DELTA);
    }

    @Test
    void testRelativeVelocityToThis() {
        assertEquals(0, relativeFrame.relativeVelocityTo(relativeFrame), ALLOWED_DELTA);
    }

    @Test
    void testRelativeVelocityToMaster() {
        assertEquals(0.5, relativeFrame.relativeVelocityTo(masterFrame), ALLOWED_DELTA);
    }

    @Test
    void testRelativeVelocityToOtherInSameDirection() {
        RelativeFrame frameInSameDirection = masterFrame.boost("fisd", 0.8);
        assertEquals(-0.5, relativeFrame.relativeVelocityTo(frameInSameDirection), ALLOWED_DELTA);
    }

    @Test
    void testRelativeVelocityToOtherInOppositeDirection() {
        RelativeFrame frameInOtherDirection = masterFrame.boost("fiod", -0.5);
        assertEquals(0.8, relativeFrame.relativeVelocityTo(frameInOtherDirection), ALLOWED_DELTA);
    }
}