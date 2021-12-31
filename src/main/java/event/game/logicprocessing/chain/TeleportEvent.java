package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.Actor;
import model.state.GameState;
import model.tile.Tile;
import model.tile.TileChunk;

public class TeleportEvent extends FixedTimeChainEvent {

	private long tileID;

	public TeleportEvent(long playerID, long tileID) {
		super(playerID);
		this.tileID = tileID;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		Actor actor = state.actor(playerID());
		actor.setChunkPos(TileChunk.chunkPos(tileID));
		actor.updatePos(Tile.tilePos(tileID));
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
