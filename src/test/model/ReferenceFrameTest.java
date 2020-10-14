package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static model.World.ALLOWED_DELTA;
import static model.ReferenceFrame.gamma;

class ReferenceFrameTest {
    @Test
    void testGamma() {
        assertEquals(1.0, gamma(0.0), ALLOWED_DELTA);
        assertEquals(1.25, gamma(0.6), ALLOWED_DELTA);
        assertEquals(5.0 / 3.0, gamma(0.8), ALLOWED_DELTA);
    }
}
