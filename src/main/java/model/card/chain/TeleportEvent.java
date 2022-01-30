package model.card.chain;

import static model.world.Tile.tileCoords;
import static model.world.TileChunk.chunkPos;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.Actor;
import model.state.GameState;

public class TeleportEvent extends FixedTimeChainEvent {

	private long tileID;

	public TeleportEvent(long playerID, long tileID) {
		super(playerID);
		this.tileID = tileID;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		Actor actor = state.actor(playerID());
		actor.worldPos().setChunkPos(chunkPos(tileID));
		actor.worldPos().setTilePos(tileCoords(tileID));
	}

	@Override
	public int priority() {
		return 9;
	}

	@Override
	public int processTime() {
		return 1;
	}

}
