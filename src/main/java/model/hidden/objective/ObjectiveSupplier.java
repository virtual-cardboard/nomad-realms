package model.hidden.objective;

import model.actor.health.cardplayer.NpcActor;
import model.state.GameState;

public interface ObjectiveSupplier {

	public Objective generate(Objective parent, NpcActor npc, GameState state);

}
