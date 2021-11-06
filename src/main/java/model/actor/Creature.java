package model.actor;

public class Creature extends CardPlayer {

	@Override
	public Creature copy() {
		return super.copyTo(new Creature());
	}

}
