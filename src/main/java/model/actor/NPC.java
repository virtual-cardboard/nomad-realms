package model.actor;

import context.game.visuals.displayer.CardPlayerDisplayer;
import context.game.visuals.displayer.NPCDisplayer;
import model.state.GameState;

public class NPC extends CardPlayer {

	private NPCDisplayer displayer;

	public NPC(int maxHealth) {
		super(maxHealth);
		displayer = new NPCDisplayer(id);
	}

	@Override
	public NPC copy(GameState state) {
		NPC copy = new NPC(maxHealth);
		copy.displayer = displayer;
		return super.copyTo(copy, state);
	}

	@Override
	public CardPlayerDisplayer<NPC> displayer() {
		return displayer;
	}

	@Override
	public String description() {
		return "An NPC with " + health + "/" + maxHealth + " health";
	}

}
