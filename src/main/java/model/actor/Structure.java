package model.actor;

import context.game.visuals.displayer.StructureDisplayer;
import model.state.GameState;
import model.structure.StructureType;

public class Structure extends HealthActor {

	private StructureType type;
	private transient StructureDisplayer displayer;

	public Structure(StructureType type) {
		super(type.health);
		this.type = type;
		displayer = new StructureDisplayer(id);
	}

	public Structure(long id, StructureType type) {
		super(id, type.health);
		this.type = type;
		displayer = new StructureDisplayer(id);
	}

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.structures().add(this);
	}

	@Override
	public Structure copy() {
		Structure copy = new Structure(id, type);
		copy.displayer = displayer;
		return super.copyTo(copy);
	}

	@Override
	public StructureDisplayer displayer() {
		return displayer;
	}

	@Override
	public String description() {
		return "A " + type + " structure with " + health + "/" + maxHealth + " health";
	}

}
