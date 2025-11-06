package engine.context.input.event;

import engine.context.input.Mouse;

public final class MouseMovedInputEvent extends GameInputEvent {

	private final Mouse mouse;
	private final int x;
	private final int y;
	private final int oldX;
	private final int oldY;

	public MouseMovedInputEvent(Mouse mouse, int mouseX, int mouseY, int oldX, int oldY) {
		this.mouse = mouse;
		this.x = mouseX;
		this.y = mouseY;
		this.oldX = oldX;
		this.oldY = oldY;
	}

	public Mouse mouse() {
		return mouse;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int offsetX() {
		return x - oldX;
	}

	public int offsetY() {
		return y - oldY;
	}

}
