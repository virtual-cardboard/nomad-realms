package model.id;

import model.actor.health.Structure;
import model.state.GameState;

public class StructureId extends EventEmitterActorId {

	public StructureId(long id) {
		super(id);
	}

	@Override
	public Structure getFrom(GameState state) {
		return state.structure(id);
	}

}
