package model.card.chain;

import static model.world.Tile.tileCoords;
import static model.world.TileChunk.chunkPos;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.StructureSpawnedSyncEvent;
import model.actor.Structure;
import model.state.GameState;
import model.structure.StructureType;

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
		return 12;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		Structure structure = new Structure(structureType);
		structure.worldPos().setChunkPos(chunkPos(tileID));
		structure.worldPos().setTilePos(tileCoords(tileID));
		state.add(structure);
		sync.add(new StructureSpawnedSyncEvent(playerID(), tileID, structure.id()));
	}

	@Override
	public int priority() {
		return 12;
	}

}
