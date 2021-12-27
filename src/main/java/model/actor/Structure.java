package model.actor;

public class Structure extends HealthActor {

	public Structure(int maxHealth) {
		super(maxHealth);
	}

	@Override
	public Structure copy() {
		return super.copyTo(new Structure(maxHealth));
	}

}
