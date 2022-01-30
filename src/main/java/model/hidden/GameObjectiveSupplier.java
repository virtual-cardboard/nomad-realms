package model.hidden;

import model.actor.NPCActor;
import model.state.GameState;

public interface GameObjectiveSupplier {

	public GameObjective createObjective(NPCActor npc, GameState state);

}
