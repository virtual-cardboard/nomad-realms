package model.actor;

import context.game.visuals.displayer.ActorDisplayer;
import model.structure.StructureType;

public class Structure extends HealthActor {

	private StructureType type;

	public Structure(StructureType type) {
		super(type.health);
		this.type = type;
	}

	@Override
	public Structure copy() {
		return super.copyTo(new Structure(type));
	}

	@Override
	public ActorDisplayer<?> displayer() {
		return null;
	}

	@Override
	public String description() {
		return "A " + type + " structure with " + health + "/" + maxHealth + " health";
	}

}
