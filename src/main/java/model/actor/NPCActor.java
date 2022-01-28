package model.actor;

import context.game.visuals.displayer.CardPlayerDisplayer;
import context.game.visuals.displayer.NPCDisplayer;
import model.ai.NPCActorAI;
import model.state.GameState;

public class NPCActor extends CardPlayer {

	protected NPCActorAI ai;
	private transient NPCDisplayer displayer;

	public NPCActor(int maxHealth) {
		super(maxHealth);
		displayer = new NPCDisplayer(id);
	}

	public NPCActor(int maxHealth, long id) {
		super(maxHealth, id);
		displayer = new NPCDisplayer(id);
	}

	@Override
	public void update(GameState state) {
		ai.update(this, state);
	}

	@Override
	public NPCActor copy() {
		NPCActor copy = new NPCActor(maxHealth, id);
		copy.displayer = displayer;
		return super.copyTo(copy);
	}

	@Override
	public CardPlayerDisplayer<NPCActor> displayer() {
		return displayer;
	}

	@Override
	public String description() {
		return "An NPC with " + health + "/" + maxHealth + " health";
	}

}
