package model.id;

import model.actor.Structure;
import model.state.GameState;

public class StructureID extends ID<Structure> {

	public StructureID(long id) {
		super(id);
	}

	@Override
	public Structure getFrom(GameState state) {
		return state.structure(id);
	}

}
