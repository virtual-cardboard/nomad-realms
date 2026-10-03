package nomadrealms.render.ui.custom.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleTest {

    @Test
    public void testSystemInputPolling() throws Exception {
        Queue<String> systemInputQueue = new ConcurrentLinkedQueue<>();
        Console console = new Console(null, null, null, systemInputQueue);

        systemInputQueue.add("HELLO");
        systemInputQueue.add("PING");

        console.pollSystemInput();

        // Check command history
        console.active(true);
        console.handleKey(GLFW_KEY_UP);
        assertEquals("PING", getCurrentInput(console));
        console.handleKey(GLFW_KEY_UP);
        assertEquals("HELLO", getCurrentInput(console));
    }

    @Test
    public void testSystemInputPollingWhenInactive() throws Exception {
        Queue<String> systemInputQueue = new ConcurrentLinkedQueue<>();
        Console console = new Console(null, null, null, systemInputQueue);
        console.active(false);

        systemInputQueue.add("HELLO");
        console.pollSystemInput();

        console.active(true);
        console.handleKey(GLFW_KEY_UP);
        assertEquals("HELLO", getCurrentInput(console));
    }

    @Test
    public void testPrintlnMirrorsToSystemOut() {
        Console console = new Console(null, null, null);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            console.println("Test line 1\nTest line 2");
            String output = outContent.toString();
            assertTrue(output.contains("Test line 1"));
            assertTrue(output.contains("Test line 2"));
        } finally {
            System.setOut(originalOut);
        }
    }

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
