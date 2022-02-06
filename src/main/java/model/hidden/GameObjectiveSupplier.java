package model.hidden;

import model.actor.NPCActor;
import model.state.GameState;

public interface GameObjectiveSupplier {

	public Objective createObjective(NPCActor npc, GameState state);

}
