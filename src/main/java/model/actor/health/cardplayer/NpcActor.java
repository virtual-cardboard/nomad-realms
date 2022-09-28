package model.actor.health.cardplayer;

import java.util.Queue;

import event.logicprocessing.CardPlayedEvent;
import model.ai.NpcActorAi;
import model.id.NPCID;
import model.state.GameState;

public abstract class NpcActor extends CardPlayer {

	protected NpcActorAi ai;

	public NpcActor(int maxHealth) {
		super(maxHealth);
	}

	public NpcActor(int maxHealth, long id) {
		super(maxHealth, id);
	}

	public void update(long tick, GameState state, Queue<CardPlayedEvent> queue) {
		CardPlayedEvent event = ai.update(this, tick, state);
		if (event != null) {
			queue.add(event);
		}
	}

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.npcs().add(this);
	}

	@Override
	public void removeFrom(GameState state) {
		super.removeFrom(state);
		state.npcs().remove(this);
	}

	@Override
	public NPCID id() {
		return new NPCID(id);
	}

	public <A extends NpcActor> A copyTo(A copy) {
		copy.ai = ai.copy();
		return super.copyTo(copy);
	}

	@Override
	public String description() {
		return "An NPC with " + health + "/" + maxHealth + " health";
	}

}
