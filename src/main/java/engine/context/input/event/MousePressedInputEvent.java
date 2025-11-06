package engine.context.input.event;

import engine.context.input.Mouse;
import org.lwjgl.glfw.GLFW;

public final class MousePressedInputEvent extends GameInputEvent {

	private Mouse mouse;
	private final int button;

	public MousePressedInputEvent(Mouse mouse, int mouseButton) {
		this.mouse = mouse;
		this.button = mouseButton;
	}

	public Mouse mouse() {
		return mouse;
	}

	/**
	 * The button that was pressed.
	 * <br><br>
	 * This is one of the following:
	 * <ul>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_1}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_2}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_3}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_4}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_5}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_6}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_7}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_8}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_LEFT}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_RIGHT}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_MIDDLE}</li>
	 *     <li>{@link GLFW#GLFW_MOUSE_BUTTON_LAST}</li>
	 * </ul>
	 *
	 * @return the button that was pressed
	 */
	public int button() {
		return button;
	}

}
