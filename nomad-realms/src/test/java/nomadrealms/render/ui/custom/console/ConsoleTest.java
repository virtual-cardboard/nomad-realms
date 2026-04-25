package nomadrealms.render.ui.custom.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class ConsoleTest {

    @Test
    public void testCommandHistory() throws Exception {
        Console console = new Console(null, null, null);
        console.active(true);

        // Type first command "HELLO"
        console.handleChar('H');
        console.handleChar('E');
        console.handleChar('L');
        console.handleChar('L');
        console.handleChar('O');
        assertEquals("HELLO", getCurrentInput(console));

        // Submit first command
        console.handleKey(GLFW_KEY_ENTER);
        assertEquals("", getCurrentInput(console));

        // Type second command "PING"
        console.handleChar('P');
        console.handleChar('I');
        console.handleChar('N');
        console.handleChar('G');
        assertEquals("PING", getCurrentInput(console));

        // Submit second command
        console.handleKey(GLFW_KEY_ENTER);
        assertEquals("", getCurrentInput(console));

        // Press UP - should get "PING"
        console.handleKey(GLFW_KEY_UP);
        assertEquals("PING", getCurrentInput(console));

        // Press UP - should get "HELLO"
        console.handleKey(GLFW_KEY_UP);
        assertEquals("HELLO", getCurrentInput(console));

        // Press UP - should still be "HELLO" (start of history)
        console.handleKey(GLFW_KEY_UP);
        assertEquals("HELLO", getCurrentInput(console));

        // Press DOWN - should get "PING"
        console.handleKey(GLFW_KEY_DOWN);
        assertEquals("PING", getCurrentInput(console));

        // Press DOWN - should get empty (original input)
        console.handleKey(GLFW_KEY_DOWN);
        assertEquals("", getCurrentInput(console));

        // Type something but don't submit: "TE"
        console.handleChar('T');
        console.handleChar('E');
        assertEquals("TE", getCurrentInput(console));

        // Press UP - should get "PING"
        console.handleKey(GLFW_KEY_UP);
        assertEquals("PING", getCurrentInput(console));

        // Press DOWN - should get back "TE"
        console.handleKey(GLFW_KEY_DOWN);
        assertEquals("TE", getCurrentInput(console));
    }

    @Test
    public void testHistoryIndexResetOnOpen() throws Exception {
        Console console = new Console(null, null, null);
        console.active(true);
        console.handleChar('H');
        console.handleKey(GLFW_KEY_ENTER); // history: ["H"]

        console.handleKey(GLFW_KEY_UP);
        assertEquals("H", getCurrentInput(console));
        assertEquals(0, getHistoryIndex(console));

        // Close console
        console.active(false);

        // Open console
        console.active(true);
        assertEquals(1, getHistoryIndex(console));
    }

    private int getHistoryIndex(Console console) throws Exception {
        Field field = Console.class.getDeclaredField("historyIndex");
        field.setAccessible(true);
        return (int) field.get(console);
    }

    private String getCurrentInput(Console console) throws Exception {
        Field field = Console.class.getDeclaredField("currentInput");
        field.setAccessible(true);
        return (String) field.get(console);
    }
}
