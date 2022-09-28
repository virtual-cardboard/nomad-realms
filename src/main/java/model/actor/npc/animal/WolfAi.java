package model.actor.npc.animal;

import event.logicprocessing.CardPlayedEvent;
import model.actor.health.cardplayer.NpcActor;
import model.ai.NpcActorAi;
import model.state.GameState;

public class WolfAi extends NpcActorAi {

	@Override
	public CardPlayedEvent playCard(NpcActor npc, GameState state) {
		return null;
	}

	@Override
	public WolfAi copy() {
		return this;
	}

	@Override
	public int genTickDelay(NpcActor npc, long tick) {
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
