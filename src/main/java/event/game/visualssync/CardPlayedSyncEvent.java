package event.game.visualssync;

import math.WorldPos;

public class CardPlayedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long cardID;
	private WorldPos pos;

	public CardPlayedSyncEvent(long playerID, long cardID, WorldPos pos) {
		super(playerID);
		this.cardID = cardID;
		this.pos = pos;
	}

	public long cardID() {
		return cardID;
	}

	public WorldPos pos() {
		return pos;
	}

}
