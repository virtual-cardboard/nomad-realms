package engine.context.input.event;

import org.lwjgl.glfw.GLFW;

/**
 * An abstract key input event is a very simple superclass for all key input events. It contains the key code of the key
 * that was involved in the event.
 * <br><br>
 * The three types of key input events are {@link KeyPressedInputEvent}, {@link KeyReleasedInputEvent}, and
 * {@link KeyRepeatedInputEvent}.
 *
 * @author Lunkle
 */
public abstract class AbstractKeyInputEvent extends GameInputEvent {

	private final int code;

	public AbstractKeyInputEvent(int code) {
		this.code = code;
	}

	/**
	 * Getter for the key code.
	 * <br><br>
	 * When comparing the key code to a <code>char</code>, always compare it with an uppercase character. E.g. 'W'
	 * instead of 'w'.
	 * <br>
	 * When checking for a non-letter character, consider comparing the key code with a GLFW constant. E.g.
	 * {@link GLFW#GLFW_KEY_LEFT_CONTROL}.
	 * <br><br>
	 * IMPORTANT: Do NOT use java's {@link java.awt.event.KeyEvent} constants. Some of them are not compatible.
	 *
	 * @return the key code
	 */
	public int code() {
		return code;
	}

}
