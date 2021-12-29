package model.actor;

import context.game.visuals.displayer.ActorDisplayer;

public class Structure extends HealthActor {

	public Structure(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public Structure copy() {
		return super.copyTo(new Structure(maxHealth));
	}

	@Override
	public ActorDisplayer<?> displayer() {
		return null;
	}

}
