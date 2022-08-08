package event.logicprocessing;

import event.NomadRealmsAsyncEvent;
import math.WorldPos;

public class SpawnPlayerAsyncEvent extends NomadRealmsAsyncEvent {

	private final WorldPos spawnPos;

	public SpawnPlayerAsyncEvent(long scheduledTick, WorldPos spawnPos) {
		super(scheduledTick);
		this.spawnPos = spawnPos;
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

}
