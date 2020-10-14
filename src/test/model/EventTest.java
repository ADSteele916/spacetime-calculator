package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static model.World.ALLOWED_DELTA;

class EventTest {
    private MasterFrame masterFrame;
    private RelativeFrame relativeFrame;
    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    void setUp() {
        masterFrame = new MasterFrame();
        relativeFrame = masterFrame.boost("rf",0.6);
        event1 = new Event("event1", 0, 0, masterFrame);
        event2 = new Event("event2", 9, 4, masterFrame);
        event3 = new Event("event3", 1, 5, relativeFrame);
    }

    @Test
    void testGetTime() {
        assertEquals(0, event1.getTime(), ALLOWED_DELTA);
        assertEquals(9, event2.getTime(), ALLOWED_DELTA);
        assertEquals(1, event3.getTime(), ALLOWED_DELTA);
    }

    @Test
    void testGetX() {
        assertEquals(0, event1.getX(), ALLOWED_DELTA);
        assertEquals(4, event2.getX(), ALLOWED_DELTA);
        assertEquals(5, event3.getX(), ALLOWED_DELTA);
    }

    @Test
    void testGetFrame() {
        assertEquals(masterFrame, event1.getFrame());
        assertEquals(masterFrame, event2.getFrame());
        assertEquals(relativeFrame, event3.getFrame());
    }

    @Test
    void testLorentzTransformSameFrame() {
        Event transformedEvent1 = event1.lorentzTransform(masterFrame);
        assertEquals("event1", transformedEvent1.getName());
        assertEquals(0, transformedEvent1.getTime(), ALLOWED_DELTA);
        assertEquals(0, transformedEvent1.getX(), ALLOWED_DELTA);
        assertEquals(masterFrame, transformedEvent1.getFrame());

        Event transformedEvent3 = event3.lorentzTransform(relativeFrame);
        assertEquals("event3", transformedEvent3.getName());
        assertEquals(1, transformedEvent3.getTime(), ALLOWED_DELTA);
        assertEquals(5, transformedEvent3.getX(), ALLOWED_DELTA);
        assertEquals(relativeFrame, transformedEvent3.getFrame());
    }

    @Test
    void testLorentzTransformNewFrame() {
        Event transformedEvent1 = event2.lorentzTransform(relativeFrame);
        assertEquals("event2", transformedEvent1.getName());
        assertEquals(8.25, transformedEvent1.getTime(), ALLOWED_DELTA);
        assertEquals(-1.75, transformedEvent1.getX(), ALLOWED_DELTA);
        assertEquals(relativeFrame, transformedEvent1.getFrame());

        Event transformedEvent3 = event3.lorentzTransform(masterFrame);
        assertEquals("event3", transformedEvent3.getName());
        assertEquals(5, transformedEvent3.getTime(), ALLOWED_DELTA);
        assertEquals(7, transformedEvent3.getX(), ALLOWED_DELTA);
        assertEquals(masterFrame, transformedEvent3.getFrame());
    }

    @Test
    void testLorentzInvariantThis() {
        assertEquals(0, event1.lorentzInvariant(event1), ALLOWED_DELTA);
    }

    @Test
    void testLorentzInvariantSameFrame() {
        assertEquals(-65, event1.lorentzInvariant(event2), ALLOWED_DELTA);
        assertEquals(-65, event2.lorentzInvariant(event1), ALLOWED_DELTA);
    }

    @Test
    void testLorentzInvariantDifferentFrame() {
        assertEquals(24, event1.lorentzInvariant(event3), ALLOWED_DELTA);
        assertEquals(-7, event2.lorentzInvariant(event3), ALLOWED_DELTA);
        assertEquals(24, event3.lorentzInvariant(event1), ALLOWED_DELTA);
        assertEquals(-7, event3.lorentzInvariant(event2), ALLOWED_DELTA);
    }

    @Test
    void testIsSimultaneousThis() {
        assertTrue(event1.isSimultaneous(event1, masterFrame));
        assertTrue(event1.isSimultaneous(event1, relativeFrame));
        assertTrue(event2.isSimultaneous(event2, masterFrame));
        assertTrue(event2.isSimultaneous(event2, relativeFrame));
    }

    @Test
    void testIsSimultaneousFail() {
        assertFalse(event1.isSimultaneous(event2, masterFrame));
        assertFalse(event1.isSimultaneous(event2, relativeFrame));
        assertFalse(event1.isSimultaneous(event3, masterFrame));
        assertFalse(event1.isSimultaneous(event3, relativeFrame));
    }

    @Test
    void testIsSimultaneousSuccess() {
        RelativeFrame simultaneousFrame = masterFrame.boost("sf",5.0 / 7.0);
        assertTrue(event1.isSimultaneous(event3, simultaneousFrame));
        assertTrue(event3.isSimultaneous(event1, simultaneousFrame));
    }

    @Test
    void testIsSamePlaceThis()  {
        assertTrue(event1.isSamePlace(event1, masterFrame));
        assertTrue(event1.isSamePlace(event1, relativeFrame));
        assertTrue(event2.isSamePlace(event2, masterFrame));
        assertTrue(event2.isSamePlace(event2, relativeFrame));
    }

    @Test
    void testIsSamePlaceFail() {
        assertFalse(event1.isSamePlace(event2, masterFrame));
        assertFalse(event1.isSamePlace(event2, relativeFrame));
        assertFalse(event1.isSimultaneous(event3, masterFrame));
        assertFalse(event1.isSimultaneous(event3, relativeFrame));
    }

    @Test
    void testIsSamePlaceSuccess() {
        RelativeFrame samePlaceFrame = masterFrame.boost("spf", -3.0 / 4.0);
        assertTrue(event2.isSamePlace(event3, samePlaceFrame));
        assertTrue(event3.isSamePlace(event2, samePlaceFrame));

    }

    @Test
    void testChangeFrameSameFrame() {
        event1.changeFrame(masterFrame);
        assertEquals(0, event1.getTime(), ALLOWED_DELTA);
        assertEquals(0, event1.getX(), ALLOWED_DELTA);
        assertEquals(masterFrame, event1.getFrame());
    }

    @Test
    void testChangeFrameNewFrame() {
        event2.changeFrame(relativeFrame);
        assertEquals(8.25, event2.getTime(), ALLOWED_DELTA);
        assertEquals(-1.75, event2.getX(), ALLOWED_DELTA);
        assertEquals(relativeFrame, event2.getFrame());
    }
}
