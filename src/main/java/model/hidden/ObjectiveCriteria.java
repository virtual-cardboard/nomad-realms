package model.hidden;

import model.actor.NPCActor;
import model.state.GameState;

public interface ObjectiveCriteria {

	public boolean test(NPCActor npc, GameState state);

}