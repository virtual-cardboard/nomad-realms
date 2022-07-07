package model.actor;

import java.util.Queue;

import event.logicprocessing.CardPlayedEvent;
import graphics.displayer.CardPlayerDisplayer;
import model.ai.NPCActorAI;
import model.id.NPCID;
import model.state.GameState;

public abstract class NpcActor extends CardPlayer {

	protected NPCActorAI ai;

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
	public NPCID id() {
		return new NPCID(id);
	}

	public <A extends NpcActor> A copyTo(A copy) {
		copy.ai = ai.copy();
		return super.copyTo(copy);
	}

	@Override
	public abstract CardPlayerDisplayer<? extends NpcActor> displayer();

	@Override
	public String description() {
		return "An NPC with " + health + "/" + maxHealth + " health";
	}

}
