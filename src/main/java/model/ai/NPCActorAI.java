package model.ai;

import java.util.Queue;

import event.game.logicprocessing.CardPlayedEvent;
import model.actor.NPCActor;
import model.hidden.GameObjective;
import model.state.GameState;

public abstract class NPCActorAI {

	private GameObjective objective;

	public void setObjective(GameObjective objective) {
		this.objective = objective;
	}

	public GameObjective objective() {
		return objective;
	}

	public abstract void update(NPCActor npc, GameState state, Queue<CardPlayedEvent> queue);

	public abstract NPCActorAI copy();

}
