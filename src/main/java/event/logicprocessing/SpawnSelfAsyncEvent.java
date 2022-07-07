package event.logicprocessing;

import event.NomadRealmsAsyncEvent;
import math.WorldPos;
import model.id.CardPlayerId;

public class SpawnSelfAsyncEvent extends NomadRealmsAsyncEvent {

	private final WorldPos spawnPos;
	private CardPlayerId playerID;

	public SpawnSelfAsyncEvent(long scheduledTick, WorldPos spawnPos) {
		super(scheduledTick);
		this.spawnPos = spawnPos;
	}

	public WorldPos spawnPos() {
		return spawnPos;
	}

	public CardPlayerId playerID() {
		return playerID;
	}

	public void setPlayerID(CardPlayerId playerID) {
		this.playerID = playerID;
	}

}
