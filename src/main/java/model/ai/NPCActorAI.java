package model.ai;

import event.game.logicprocessing.CardPlayedEvent;
import model.actor.NPCActor;
import model.hidden.GameObjective;
import model.state.GameState;

public abstract class NPCActorAI {

	protected int tickDelayTimer = 0;
	protected GameObjective objective;

	public void setObjective(GameObjective objective) {
		this.objective = objective;
	}

	public GameObjective objective() {
		return objective;
	}

	public CardPlayedEvent update(NPCActor npc, GameState state) {
		System.out.println(npc + " " + tickDelayTimer);
		if (tickDelayTimer > 0) {
			tickDelayTimer--;
			return null;
		}
		tickDelayTimer = genTickDelay();
		return playCard(npc, state);
	}

	public abstract CardPlayedEvent playCard(NPCActor npc, GameState state);

	public abstract NPCActorAI copy();

	public <A extends NPCActorAI> A copyTo(A ai) {
		ai.tickDelayTimer = tickDelayTimer;
		System.out.println("Copy ticktimer " + tickDelayTimer);
		ai.objective = objective;
		return ai;
	}

	/**
	 * Returns the amount of ticks to wait before playing the next card.
	 * 
	 * @return
	 */
	public abstract int genTickDelay();

}
