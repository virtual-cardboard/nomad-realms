package model.hidden;

import model.actor.NPCActor;
import model.state.GameState;

public enum GameObjectiveType {

	GATHER_WOOD((npc, state) -> new CutTreeObjective()),
	CUT_TREE((npc, state) -> new CutTreeObjective());

	private GameObjectiveSupplier supplier;

	GameObjectiveType(GameObjectiveSupplier supplier) {
		this.supplier = supplier;
	}

	GameObjective createObjective(NPCActor npc, GameState state) {
		return supplier.createObjective(npc, state);
	}

}
