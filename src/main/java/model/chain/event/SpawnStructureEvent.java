package model.chain.event;

import engine.common.ContextQueues;
import event.sync.StructureSpawnedSyncEvent;
import math.IdGenerators;
import model.actor.Structure;
import model.chain.EffectChain;
import model.id.CardPlayerId;
import model.id.TileId;
import model.state.GameState;
import model.structure.StructureType;

public class SpawnStructureEvent extends FixedTimeChainEvent {

	private TileId tileID;
	private StructureType structureType;

	public SpawnStructureEvent(CardPlayerId playerID, TileId tileID, StructureType structureType) {
		super(playerID);
		this.tileID = tileID;
		this.structureType = structureType;
	}

	@Override
	public int processTime() {
		return 12;
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, ContextQueues contextQueues) {
		Structure structure = new Structure(structureType);
		structure.setId(idGenerators.personalIdGenerator().genId()); // TODO: CHANGE ME!!!!
		structure.worldPos().set(tileID.getFrom(state).worldPos());
		state.add(structure);
		if (structureType.onSummon != null) {
			EffectChain chain = new EffectChain();
			chain.addAllWheneverEvents(structureType.onSummon.apply(structure, state));
		}
		contextQueues.pushEventFromLogic(new StructureSpawnedSyncEvent(playerID(), tileID, structure.id()));
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
