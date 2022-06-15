package event.logicprocessing;

import event.NomadRealmsAsyncEvent;
import math.WorldPos;
import model.id.CardPlayerID;

public class SpawnSelfAsyncEvent extends NomadRealmsAsyncEvent {

	private final WorldPos spawnPos;
	private CardPlayerID playerID;

	public SpawnSelfAsyncEvent(long scheduledTick, WorldPos spawnPos) {
		super(scheduledTick);
		this.spawnPos = spawnPos;
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

	public CardPlayerID playerID() {
		return playerID;
	}

	public void setPlayerID(CardPlayerID playerID) {
		this.playerID = playerID;
	}

}
