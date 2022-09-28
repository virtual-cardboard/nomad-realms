package model.hidden.objective;

import model.actor.health.cardplayer.NpcActor;
import model.state.GameState;

public interface ObjectiveCriteria {

	public boolean test(NpcActor npc, GameState state);

}
