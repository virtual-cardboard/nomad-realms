package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import event.game.visualssync.StructureSpawnedSyncEvent;
import model.actor.Structure;
import model.id.CardPlayerID;
import model.id.TileID;
import model.state.GameState;
import model.structure.StructureType;

public class SpawnStructureEvent extends FixedTimeChainEvent {

	private TileID tileID;
	private StructureType structureType;

	public SpawnStructureEvent(CardPlayerID playerID, TileID tileID, StructureType structureType) {
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
		structure.worldPos().set(tileID.getFrom(state).worldPos());
		state.add(structure);
		sync.add(new StructureSpawnedSyncEvent(playerID(), tileID, structure.id()));
	}

	@Override
	public int priority() {
		return 12;
	}

	@Override
	public String textureName() {
		return "spawn_structure";
	}

}
