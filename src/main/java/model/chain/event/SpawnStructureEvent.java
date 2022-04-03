package model.chain.event;

import common.QueueGroup;
import event.game.sync.StructureSpawnedSyncEvent;
import model.actor.Structure;
import model.chain.EffectChain;
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
	public void process(long tick, GameState state, QueueGroup queueGroup) {
		Structure structure = new Structure(structureType);
		structure.worldPos().set(tileID.getFrom(state).worldPos());
		state.add(structure);
		if (structureType.onSummon != null) {
			EffectChain chain = new EffectChain();
			chain.addAllWhenever(structureType.onSummon.apply(structure, state));
		}
		queueGroup.pushEventFromLogic(new StructureSpawnedSyncEvent(playerID(), tileID, structure.id()));
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
