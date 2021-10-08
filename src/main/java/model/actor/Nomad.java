package model.actor;

public class Nomad extends CardPlayer {

	public Nomad() {
	}

	@Override
	public Nomad copy() {
		return super.copyTo(new Nomad());
	}

	@Override
	public String getDescription() {
		return "A nomad";
	}

}
