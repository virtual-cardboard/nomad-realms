package model.actor.npc.animal;

import event.logicprocessing.CardPlayedEvent;
import model.actor.NPCActor;
import model.ai.NPCActorAI;
import model.state.GameState;

public class GoatAI extends NPCActorAI {

	@Override
	public CardPlayedEvent playCard(NPCActor npc, GameState state) {
		return null;
	}

	@Override
	public GoatAI copy() {
		return this;
	}

	@Override
	public int genTickDelay(NPCActor npc, long tick) {
		return 10 + (int) (Math.random() * 10);
	}

	@Override
	public void generateSubObjectives() {
		switch (objective.type()) {
			default:
				break;
		}
	}

}
