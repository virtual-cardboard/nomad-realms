package nomadrealms.render.ui;

import org.junit.jupiter.api.Test;
import engine.visuals.constraint.box.ConstraintPair;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CameraTest {

    @Test
    public void testZoomScaling() {
        Camera camera = new Camera(0, 0);
        camera.right(true);

        // Zoom 1
        camera.zoom(1);
        camera.update();
        float x1 = camera.position().x().get();

        // Reset
        camera = new Camera(0, 0);
        camera.right(true);

        // Zoom 0.5 (Zoomed out) - Should be faster
        camera.zoom(0.5f);
        camera.update();
        float x05 = camera.position().x().get();

        // Check relative speed
        assertTrue(x05 > x1, "Zoomed out (0.5) should be faster than Zoom 1");

        // Reset
        camera = new Camera(0, 0);
        camera.right(true);

        // Zoom 2 (Zoomed in) - Should be slower
        camera.zoom(2f);
        camera.update();
        float x2 = camera.position().x().get();

        assertTrue(x2 < x1, "Zoomed in (2.0) should be slower than Zoom 1");
    }

    @Test
    public void testMoveSpeedBug() {
        Camera camera = new Camera(0, 0);
        camera.right(true);
        camera.moveSpeed(100f);
        camera.update();
        float x100 = camera.position().x().get();

        camera = new Camera(0, 0);
        camera.right(true);
        camera.moveSpeed(10f);
        camera.update();
        float x10 = camera.position().x().get();

        // This used to fail because moveSpeed was ignored due to a bug.
        // Now it should work: x100 should be roughly 10 times x10
        // We allow some delta for float precision and the way updates work
        assertEquals(x10 * 10, x100, 0.1f, "Move speed should affect position change");
    }
}
