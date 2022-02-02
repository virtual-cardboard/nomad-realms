package model.actor;

import java.util.Queue;

import context.game.visuals.displayer.CardPlayerDisplayer;
import event.game.logicprocessing.CardPlayedEvent;
import model.ai.NPCActorAI;
import model.state.GameState;

public abstract class NPCActor extends CardPlayer {

	protected NPCActorAI ai;

	public NPCActor(int maxHealth) {
		super(maxHealth);
	}

	public NPCActor(int maxHealth, long id) {
		super(maxHealth, id);
	}

	public void update(GameState state, Queue<CardPlayedEvent> queue) {
		CardPlayedEvent event = ai.update(this, state);
		if (event != null) {
			queue.add(event);
		}
	}

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.npcs().add(this);
	}

	public <A extends NPCActor> A copyTo(A copy) {
		copy.ai = ai.copy();
		return super.copyTo(copy);
	}

	@Override
	public abstract CardPlayerDisplayer<? extends NPCActor> displayer();

	@Override
	public String description() {
		return "An NPC with " + health + "/" + maxHealth + " health";
	}

}
