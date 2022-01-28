package model.actor;

import context.game.visuals.displayer.CardPlayerDisplayer;
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

	@Override
	public void update(GameState state) {
		ai.update(this, state);
	}

	@Override
	public abstract CardPlayerDisplayer<? extends NPCActor> displayer();

	@Override
	public String description() {
		return "An NPC with " + health + "/" + maxHealth + " health";
	}

}
