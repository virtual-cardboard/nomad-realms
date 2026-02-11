package engine.context.input.event;

import engine.context.input.Mouse;

public final class MouseReleasedInputEvent extends GameInputEvent {

	private Mouse mouse;
	private final int button;

	public MouseReleasedInputEvent(Mouse mouse, int mouseButton) {
		this.mouse = mouse;
		this.button = mouseButton;
	}

	public Mouse mouse() {
		return mouse;
	}

	public int button() {
		return button;
	}

}
