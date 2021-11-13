package model.actor;

public class Nomad extends CardPlayer {

	public Nomad() {
		super(20);
	}

	@Override
	public Nomad copy() {
		return super.copyTo(new Nomad());
	}

	@Override
	public String description() {
		return "A nomad";
	}

}
