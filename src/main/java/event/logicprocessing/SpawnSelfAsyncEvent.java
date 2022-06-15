package event.logicprocessing;

import engine.common.event.async.AsyncGameEvent;
import math.WorldPos;

public class SpawnSelfAsyncEvent extends AsyncGameEvent {

	private final WorldPos spawnPos;

	public SpawnSelfAsyncEvent(long scheduledTick, WorldPos spawnPos) {
		super(scheduledTick);
		this.spawnPos = spawnPos;
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

}
