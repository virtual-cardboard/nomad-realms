package model.actor;

import context.game.visuals.displayer.CardPlayerDisplayer;
import context.game.visuals.displayer.NPCDisplayer;

public class NPC extends CardPlayer {

	private transient NPCDisplayer displayer;

	public NPC(int maxHealth) {
		super(maxHealth);
		displayer = new NPCDisplayer(id);
	}

	public NPC(int maxHealth, long id) {
		super(maxHealth, id);
		displayer = new NPCDisplayer(id);
	}

	@Override
	public NPC copy() {
		NPC copy = new NPC(maxHealth, id);
		copy.displayer = displayer;
		return super.copyTo(copy);
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
