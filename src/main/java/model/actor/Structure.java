package model.actor;

import context.game.visuals.displayer.ActorDisplayer;
import model.state.GameState;

public class Structure extends HealthActor {

	public Structure(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public Structure copy(GameState state) {
		return super.copyTo(new Structure(maxHealth));
	}

	@Override
	public ActorDisplayer<?> displayer() {
		return null;
	}

	@Override
	public String description() {
		return "A structure with " + health + "/" + maxHealth + " health";
	}

}
