package model.actor;

import context.game.visuals.displayer.CreatureDisplayer;
import context.visuals.lwjgl.Texture;

public class Creature extends CardPlayer {

	private transient CreatureDisplayer displayer;

	public Creature(int maxHealth, Texture texture) {
		super(maxHealth);
		displayer = new CreatureDisplayer(this, texture);
	}

	@Override
	public CreatureDisplayer displayer() {
		return displayer;
	}

	@Override
	public Creature copy() {
		return super.copyTo(new Creature(maxHealth, displayer.texture()));
	}

}
