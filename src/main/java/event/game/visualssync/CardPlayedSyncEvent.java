package event.game.visualssync;

import common.math.Vector2f;
import common.math.Vector2i;

public class CardPlayedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long cardID;
	private Vector2i chunkPos;
	private Vector2f pos;

	public CardPlayedSyncEvent(long playerID, long cardID, Vector2i chunkPos, Vector2f pos) {
		super(playerID);
		this.cardID = cardID;
		this.chunkPos = chunkPos;
		this.pos = pos;
	}

	public long cardID() {
		return cardID;
	}

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public Vector2f pos() {
		return pos;
	}

}
