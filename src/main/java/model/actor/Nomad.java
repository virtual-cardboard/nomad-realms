package model.actor;

import context.game.visuals.displayer.NomadDisplayer;
import model.state.GameState;

public class Nomad extends CardPlayer {

	private transient NomadDisplayer nomadDisplayer;

	public Nomad() {
		super(20);
		nomadDisplayer = new NomadDisplayer(id);
	}

	@Override
	public Nomad copy(GameState state) {
		Nomad copy = new Nomad();
		copy.nomadDisplayer = nomadDisplayer;
		return super.copyTo(copy, state);
	}

	@Override
	public NomadDisplayer displayer() {
		return nomadDisplayer;
	}

	@Override
	public String description() {
		return "A nomad with " + health + "/" + maxHealth + " health";
	}

}
