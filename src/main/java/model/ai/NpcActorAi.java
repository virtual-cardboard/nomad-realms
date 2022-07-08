package model.ai;

import event.logicprocessing.CardPlayedEvent;
import model.actor.NpcActor;
import model.hidden.objective.Objective;
import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveType;
import model.state.GameState;

public abstract class NpcActorAi {

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

	public CardPlayedEvent update(NpcActor npc, long tick, GameState state) {
		if (tickDelayTimer > 0) {
			tickDelayTimer--;
			return null;
		}
		tickDelayTimer = genTickDelay(npc, tick);
		return playCard(npc, state);
	}

	public abstract CardPlayedEvent playCard(NpcActor npc, GameState state);

	public abstract NpcActorAi copy();

	public <A extends NpcActorAi> A copyTo(A ai) {
		ai.tickDelayTimer = tickDelayTimer;
		ai.objective = objective;
		return ai;
	}

	/**
	 * Returns the amount of ticks to wait before playing the next card.
	 *
	 * @param npc  the npc
	 * @param tick the current tick
	 * @return
	 */
	public abstract int genTickDelay(NpcActor npc, long tick);

	public abstract void generateSubObjectives();

}