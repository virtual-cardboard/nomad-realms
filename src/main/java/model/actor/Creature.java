package model.actor;

public class Creature extends CardPlayer {

	public Creature(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public Creature copy() {
		return super.copyTo(new Creature(maxHealth));
	}

}
