package nomadrealms.context.game;

/**
 * Stores the last N game states in serialized form.
 * The states are stored BEFORE they are updated, so this history EXCLUDES the current game state.
 */
public class GameStateHistory {

	public static final int DEFAULT_MAX_HISTORY_SIZE = 20;

	private final byte[][] history;
	// Represents the frame number of the oldest recorded state in the buffer
	private long oldestFrameNumber = -1;
	// Represents the frame number of the most recently recorded state in the buffer
	private long newestFrameNumber = -1;
	private int head = 0;
	private int count = 0;

	public GameStateHistory() {
		this(DEFAULT_MAX_HISTORY_SIZE);
	}

	public GameStateHistory(int maxHistorySize) {
		this.history = new byte[maxHistorySize][];
	}

	public void push(GameState gameState) {
		if (count > 0 && gameState.frameNumber != newestFrameNumber + 1) {
			throw new IllegalStateException("Pushed game state must have exactly the next consecutive frame number.");
		}
		byte[] serialized = GameStateDerializer.serialize(gameState);
		history[head] = serialized;
		newestFrameNumber = gameState.frameNumber;
		if (count == 0) {
			oldestFrameNumber = gameState.frameNumber;
		} else if (count == history.length) {
			oldestFrameNumber++;
		}
		head = (head + 1) % history.length;
		if (count < history.length) {
			count++;
		}
	}

	public boolean hasGameState(long frameNumber) {
		return count > 0 && frameNumber >= oldestFrameNumber && frameNumber <= newestFrameNumber;
	}

	/**
	 * Retrieves a serialized GameState from the history in O(1) time.
	 *
	 * The history is stored in a circular buffer. We compute the physical index
	 * in the array by taking the most recently added position (`head - 1`) and
	 * subtracting the difference between the most recent frame number and the
	 * requested frame number. Modulo arithmetic handles the wrapping around the
	 * circular buffer.
	 */
	public GameState getGameState(long frameNumber) {
		if (!hasGameState(frameNumber)) {
			throw new IllegalArgumentException("GameState for frameNumber " + frameNumber + " not found in history.");
		}
		int index = (head - 1 - (int)(newestFrameNumber - frameNumber)) % history.length;
		if (index < 0) {
			index += history.length;
		}
		return GameStateDerializer.deserialize(history[index]);
	}

	public long oldestFrameNumber() {
		return oldestFrameNumber;
	}

	public long newestFrameNumber() {
		return newestFrameNumber;
	}

}
