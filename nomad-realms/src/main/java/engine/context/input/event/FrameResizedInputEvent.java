package engine.context.input.event;

public final class FrameResizedInputEvent extends GameInputEvent {

	private final int width;
	private final int height;

	public FrameResizedInputEvent(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

}
