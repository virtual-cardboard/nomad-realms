package engine.context.input.event;

import engine.context.input.Mouse;

public final class MouseScrolledInputEvent extends GameInputEvent {

	private final Mouse mouse;
	private final float xAmount;
	private final float yAmount;

	public MouseScrolledInputEvent(Mouse mouse, float xAmount, float yAmount) {
		this.mouse = mouse;
		this.xAmount = xAmount;
		this.yAmount = yAmount;
	}

	public Mouse mouse() {
		return mouse;
	}

	public float xAmount() {
		return xAmount;
	}

	public float yAmount() {
		return yAmount;
	}

}
