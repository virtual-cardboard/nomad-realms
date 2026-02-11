package nomadrealms.context.game.event;

import java.util.List;

public class InputEventFrame {

	private long frameNumber;
	private List<InputEvent> events;

	/**
	 * No-arg constructor for serialization.
	 */
	private InputEventFrame() {
	}

	public InputEventFrame(long frameNumber) {
		this.frameNumber = frameNumber;
	}

	public void addEvent(InputEvent event) {
		events.add(event);
	}

}
