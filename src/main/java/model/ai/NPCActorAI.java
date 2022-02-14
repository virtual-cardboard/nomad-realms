package model.ai;

import event.game.logicprocessing.CardPlayedEvent;
import model.actor.NPCActor;
import model.hidden.Objective;
import model.hidden.ObjectiveCriteria;
import model.hidden.ObjectiveType;
import model.state.GameState;

public abstract class NPCActorAI {

	protected int tickDelayTimer = 0;
	protected Objective objective;

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public void setObjective(ObjectiveType type, ObjectiveCriteria completionCriteria) {
		this.objective = new Objective(type, null, completionCriteria);
	}

	public Objective objective() {
		return objective;
	}

	public CardPlayedEvent update(NPCActor npc, GameState state) {
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
		ai.objective = objective;
		return ai;
	}

	/**
	 * Returns the amount of ticks to wait before playing the next card.
	 * 
	 * @return
	 */
	public abstract int genTickDelay();

	public abstract void generateSubObjectives();

}
