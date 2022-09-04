package model.actor;

import java.util.ArrayList;
import java.util.List;

import derealizer.format.SerializationFormatEnum;
import model.id.StructureId;
import model.state.GameState;
import model.structure.StructureType;

public class Structure extends EventEmitterActor {

	private final StructureType type;

	public Structure(StructureType type) {
		super(type.health);
		this.type = type;
	}

	public Structure(long id, StructureType type) {
		super(id, type.health);
		this.type = type;
	}

	@Override
	public StructureId id() {
		return new StructureId(id);
	}

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.structures().add(this);
		// Get the list of structures at this chunk, or create a new one if it doesn't exist
		List<Structure> list = state.chunkToStructures().computeIfAbsent(worldPos().chunkPos(), k -> new ArrayList<>());
		list.add(this);
	}

	@Override
	public void removeFrom(GameState state) {
		super.removeFrom(state);
		// Remove this structure from the state's index
		state.structures().remove(this);
		state.chunkToStructures().get(worldPos().chunkPos()).remove(this);
	}

	@Override
	public Structure copy() {
		Structure copy = new Structure(id, type);
		copy.setDisplayer(displayer());
		return super.copyTo(copy);
	}

	public StructureType type() {
		return type;
	}

	@Override
	public String description() {
		return "A " + type + " structure with " + health + "/" + maxHealth + " health";
	}

	@Override
	public void update(long tick, GameState state) {
		// TODO Auto-generated method stub
	}

	@Override
	public SerializationFormatEnum formatEnum() {
		return null;
	}

}
