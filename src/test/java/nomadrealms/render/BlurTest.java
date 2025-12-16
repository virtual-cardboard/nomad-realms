package nomadrealms.render;

import engine.nengen.Nengen;
import experimental.shader.blur.BlurContext;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class BlurTest {

    @Test
    public void testBlurRendering() throws Exception {
        // Only run if not headless? Or assume we can run.
        // If this runs in a CI without display, it might fail.
        // We can check for a system property to skip if needed, but for now we try.

        BlurContext context = new BlurContext();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<BufferedImage> screenshotRef = new AtomicReference<>();
        AtomicReference<Exception> errorRef = new AtomicReference<>();

        Thread gameThread = new Thread(() -> {
            try {
                Nengen nengen = new Nengen();
                nengen.configure()
                        .setWindowDim(800, 600)
                        .setWindowName("Blur Test")
                        .setFrameRate(60)
                        .setTickRate(10);

                // Inject the hook
                context.onPostRender = () -> {
                    try {
                        // Take screenshot only once
                        if (latch.getCount() > 0) {
                            BufferedImage screenshot = ScreenshotHelper.takeScreenshot(800, 600);
                            screenshotRef.set(screenshot);
                            latch.countDown();

                            // Exit logic?
                            // Nengen doesn't seem to have a stop() method we can call easily from here without access to internal loop control.
                            // However, if we throw a RuntimeException, it might crash the loop.
                            // Or better, we just let it run until we kill the thread or exit process?
                            // JUnit usually waits for the test method to return.
                            // If we return, the test passes. The background thread might keep running.
                            // This is messy. Ideally we should stop Nengen.
                            // Let's assume we can call System.exit(0) is too harsh for unit tests.
                            // If Nengen checks a flag in GameContext? No.
                        }
                    } catch (Exception e) {
                        errorRef.set(e);
                        latch.countDown();
                    }
                };

                nengen.startNengen(context);
            } catch (Exception e) {
                // Ignore interruption or forceful stop
            }
        });

        gameThread.setDaemon(true); // Ensure it dies when test finishes
        gameThread.start();

        // Wait for screenshot
        boolean success = latch.await(10, TimeUnit.SECONDS);
        if (!success) {
            fail("Timed out waiting for screenshot");
        }

        if (errorRef.get() != null) {
            throw errorRef.get();
        }

        BufferedImage screenshot = screenshotRef.get();
        assertNotNull(screenshot, "Screenshot should not be null");

        File goldenFile = new File("src/test/resources/goldens/blur_test.png");

        if (Boolean.getBoolean("UPDATE_GOLDENS") || !goldenFile.exists()) {
            ScreenshotHelper.saveImage(screenshot, goldenFile);
            System.out.println("Golden image updated: " + goldenFile.getAbsolutePath());
        } else {
            BufferedImage golden = javax.imageio.ImageIO.read(goldenFile);
            if (!ScreenshotHelper.compareImages(golden, screenshot)) {
                // Save the failing image for inspection
                File failureFile = new File("target/failures/blur_test_fail.png");
                ScreenshotHelper.saveImage(screenshot, failureFile);
                fail("Screenshot does not match golden! Saved failure to " + failureFile.getAbsolutePath());
            }
        }
    }
}
