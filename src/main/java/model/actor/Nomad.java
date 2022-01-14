package model.actor;

import context.game.visuals.displayer.NomadDisplayer;

public class Nomad extends CardPlayer {

	private transient NomadDisplayer displayer;

	public Nomad() {
		super(20);
		displayer = new NomadDisplayer(id);
	}

	public Nomad(long id) {
		super(20, id);
		displayer = new NomadDisplayer(id);
	}

	public Nomad(long id, NomadDisplayer displayer) {
		super(20, id);
		this.displayer = displayer;
	}

	@Override
	public Nomad copy() {
		Nomad copy = new Nomad(id, displayer);
		return super.copyTo(copy);
	}

	@Override
	public NomadDisplayer displayer() {
		return displayer;
	}

	@Override
	public String description() {
		return "A nomad with " + health + "/" + maxHealth + " health";
	}

}
