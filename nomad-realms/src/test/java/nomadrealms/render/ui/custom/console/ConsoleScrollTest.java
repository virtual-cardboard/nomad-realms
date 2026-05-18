package nomadrealms.render.ui.custom.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class ConsoleScrollTest {

    @Test
    public void testScrollClamping() throws Exception {
        Console console = new Console(null, null, null);
        console.consoleHeight(300);

        // Initial scroll should be 0
        assertEquals(0, getScrollOffset(console));

        // Adding few lines, total height should be less than visible height (300 - 40 - 5 = 255)
        // 8 lines * 30 = 240
        for (int i = 0; i < 8; i++) {
            console.println("Line " + i);
        }

        // Try to scroll down, should be clamped to 0 because totalHeight < visibleHeight
        console.handleScroll(10);
        assertEquals(0, getScrollOffset(console));

        // Add more lines so totalHeight > visibleHeight
        // 20 lines * 30 = 600. maxScroll = 600 - 255 = 345
        for (int i = 8; i < 20; i++) {
            console.println("Line " + i);
        }

        // Scroll down
        console.handleScroll(5); // 5 * 20 = 100
        assertEquals(100, getScrollOffset(console));

        // Scroll up past 0
        console.handleScroll(-10); // 100 - 200 = -100 -> clamped to 0
        assertEquals(0, getScrollOffset(console));

        // Scroll down past maxScroll (345)
        console.handleScroll(20); // 0 + 400 = 400 -> clamped to 345
        assertEquals(345, getScrollOffset(console));
    }

    private float getScrollOffset(Console console) throws Exception {
        Field field = Console.class.getDeclaredField("scrollOffset");
        field.setAccessible(true);
        return (float) field.get(console);
    }
}
