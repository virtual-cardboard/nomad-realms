package model.actor;

import context.game.visuals.displayer.CardPlayerDisplayer;
import context.game.visuals.displayer.NPCDisplayer;

public class NPC extends CardPlayer {

	private NPCDisplayer displayer = new NPCDisplayer(this);

	public NPC(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public CardPlayer copy() {
		return super.copyTo(this);
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
