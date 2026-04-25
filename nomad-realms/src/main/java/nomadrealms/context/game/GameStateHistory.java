package nomadrealms.context.game;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameStateHistory {

    public static final int DEFAULT_MAX_HISTORY_SIZE = 20;

    private final int maxHistorySize;
    private final Map<Long, byte[]> history;

    public GameStateHistory() {
        this(DEFAULT_MAX_HISTORY_SIZE);
    }

    public GameStateHistory(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
        this.history = new LinkedHashMap<Long, byte[]>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, byte[]> eldest) {
                return size() > maxHistorySize;
            }
        };
    }

    public void push(GameState gameState) {
        byte[] serialized = GameStateDerializer.serialize(gameState);
        history.put(gameState.frameNumber, serialized);
    }

    public boolean hasGameState(long frameNumber) {
        return history.containsKey(frameNumber);
    }

    public GameState getGameState(long frameNumber) {
        byte[] serialized = history.get(frameNumber);
        if (serialized == null) {
            throw new IllegalArgumentException("GameState for frameNumber " + frameNumber + " not found in history.");
        }
        return GameStateDerializer.deserialize(serialized);
    }
}
