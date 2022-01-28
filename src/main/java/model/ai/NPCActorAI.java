package model.ai;

import model.actor.NPCActor;
import model.state.GameState;

public interface NPCActorAI {

	public void update(NPCActor npc, GameState state);

	public NPCActorAI copy();

}
