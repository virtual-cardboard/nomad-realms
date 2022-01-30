package model.hidden;

import model.actor.NPCActor;
import model.state.GameState;

public enum GameObjectiveType {

	COMMAND_BUILD_HOUSE(),
	GATHER_WOOD((npc, state) -> new CutTreeObjective()),
	CUT_TREE((npc, state) -> new CutTreeObjective());

	private GameObjectiveSupplier supplier;

	GameObjectiveType() {
	}

	GameObjectiveType(GameObjectiveSupplier supplier) {
		this.supplier = supplier;
	}

	GameObjective createObjective(NPCActor npc, GameState state) {
		if (supplier == null) {
			return new LeafNodeObjective(this);
		}
		return supplier.createObjective(npc, state);
	}

}
