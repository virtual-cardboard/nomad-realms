package event.game.logicprocessing.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.StructureSpawnedSyncEvent;
import model.actor.Structure;
import model.state.GameState;
import model.structure.StructureType;
import model.world.Tile;
import model.world.TileChunk;

public class SpawnStructureEvent extends FixedTimeChainEvent {

	private long tileID;
	private StructureType structureType;

	public SpawnStructureEvent(long playerID, long tileID, StructureType structureType) {
		super(playerID);
		this.tileID = tileID;
		this.structureType = structureType;
	}

	@Override
	public int processTime() {
		return 4;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		Structure structure = new Structure(structureType);
		structure.setChunkPos(TileChunk.chunkPos(tileID));
		structure.updatePos(Tile.tilePos(tileID));
		state.add(structure);
		sync.add(new StructureSpawnedSyncEvent(playerID(), tileID, structure.id()));
	}

	@Override
	public int priority() {
		return 12;
	}

}
