package model.actor;

import context.game.visuals.displayer.CreatureDisplayer;

public class Creature extends CardPlayer {

	private transient CreatureDisplayer displayer = new CreatureDisplayer(this);

	public Creature(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public CreatureDisplayer displayer() {
		return displayer;
	}

	@Override
	public Creature copy() {
		return super.copyTo(new Creature(maxHealth));
	}

}
