package model.actor;

import context.game.visuals.displayer.NomadDisplayer;

public class Nomad extends CardPlayer {

	private transient NomadDisplayer nomadDisplayer = new NomadDisplayer(this);

	public Nomad() {
		super(20);
	}

	@Override
	public Nomad copy() {
		return super.copyTo(new Nomad());
	}

	@SuppressWarnings("unchecked")
	@Override
	public NomadDisplayer displayer() {
		return nomadDisplayer;
	}

	@Override
	public String description() {
		return "A nomad";
	}

}
