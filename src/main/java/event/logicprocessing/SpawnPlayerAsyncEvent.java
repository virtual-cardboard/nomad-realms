package event.logicprocessing;

import engine.common.event.async.AsyncGameEvent;
import math.WorldPos;

public class SpawnPlayerAsyncEvent extends AsyncGameEvent {

	private final WorldPos spawnPos;

	public SpawnPlayerAsyncEvent(long scheduledTick, WorldPos spawnPos) {
		super(scheduledTick);
		this.spawnPos = spawnPos;
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

}
