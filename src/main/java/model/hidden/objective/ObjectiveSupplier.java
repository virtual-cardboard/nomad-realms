package model.hidden.objective;

import model.actor.NPCActor;
import model.state.GameState;

public interface ObjectiveSupplier {

	public Objective generate(Objective parent, NPCActor npc, GameState state);

}
