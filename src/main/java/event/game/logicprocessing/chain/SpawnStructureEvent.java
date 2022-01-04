package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.StructureSpawnedSyncEvent;
import model.actor.Structure;
import model.state.GameState;
import model.world.Tile;
import model.world.TileChunk;

public class SpawnStructureEvent extends FixedTimeChainEvent {

	private long tileID;
	private Structure structure;

	public SpawnStructureEvent(long playerID, long tileID, Structure structure) {
		super(playerID);
		this.tileID = tileID;
		this.structure = structure;
	}

	@Override
	public int processTime() {
		return 4;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		structure.setChunkPos(TileChunk.chunkPos(tileID));
		structure.updatePos(Tile.tilePos(tileID));
		state.add(structure);
		sync.add(new StructureSpawnedSyncEvent(player(), target, structure));
	}

	@Override
	public int priority() {
		return 12;
	}

}
