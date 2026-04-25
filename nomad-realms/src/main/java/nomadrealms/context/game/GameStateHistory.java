package nomadrealms.context.game;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameStateHistory {

    private final int maxHistorySize;
    private final Map<Long, byte[]> history;

    public GameStateHistory(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
        this.history = new LinkedHashMap<Long, byte[]>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, byte[]> eldest) {
                return size() > maxHistorySize;
            }
        };
    }

    public void addGameState(GameState gameState) {
        byte[] serialized = GameStateDerializer.serialize(gameState);
        history.put(gameState.frameNumber, serialized);
    }

    public boolean hasGameState(long tick) {
        return history.containsKey(tick);
    }

    public GameState getGameState(long tick) {
        byte[] serialized = history.get(tick);
        if (serialized == null) {
            throw new IllegalArgumentException("GameState for tick " + tick + " not found in history.");
        }
        return GameStateDerializer.deserialize(serialized);
    }
}
