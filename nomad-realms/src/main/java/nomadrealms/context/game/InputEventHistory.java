package nomadrealms.context.game;

import nomadrealms.context.game.event.InputEventFrame;

/**
 * Stores the last N input event frames.
 */
public class InputEventHistory {

	public static final int DEFAULT_MAX_HISTORY_SIZE = 20;

	private final InputEventFrame[] frames;
	private long oldestFrameNumber = -1;
	private long newestFrameNumber = -1;
	private int head = 0;
	private int count = 0;

	public InputEventHistory() {
		this(DEFAULT_MAX_HISTORY_SIZE);
	}

	public InputEventHistory(int maxHistorySize) {
		this.frames = new InputEventFrame[maxHistorySize];
	}

	public void push(InputEventFrame frame) {
		long frameNumber = frame.frameNumber();
		frames[head] = frame;
		newestFrameNumber = frameNumber;
		if (count == 0) {
			oldestFrameNumber = frameNumber;
		} else if (count == frames.length) {
			oldestFrameNumber++;
		}
		head = (head + 1) % frames.length;
		if (count < frames.length) {
			count++;
		}
	}

	public boolean hasInputFrame(long frameNumber) {
		return count > 0 && frameNumber >= oldestFrameNumber && frameNumber <= newestFrameNumber;
	}

	/**
	 * Retrieves an InputEventFrame from the history in O(1) time.
	 *
	 * The history is stored in a circular buffer. We compute the physical index
	 * in the array by taking the most recently added position (`head - 1`) and
	 * subtracting the difference between the most recent frame number and the
	 * requested frame number. Modulo arithmetic handles the wrapping around the
	 * circular buffer.
	 */
	public InputEventFrame getInputFrame(long frameNumber) {
		if (!hasInputFrame(frameNumber)) {
			throw new IllegalArgumentException("InputEventFrame for frameNumber " + frameNumber + " not found in history.");
		}
		int index = (head - 1 - (int)(newestFrameNumber - frameNumber)) % frames.length;
		if (index < 0) {
			index += frames.length;
		}
		return frames[index];
	}

	public long oldestFrameNumber() {
		return oldestFrameNumber;
	}

	public long newestFrameNumber() {
		return newestFrameNumber;
	}

}
