package model.actor;

import java.util.ArrayList;
import java.util.List;

import derealizer.format.SerializationFormatEnum;
import graphics.displayer.StructureDisplayer;
import model.id.StructureId;
import model.state.GameState;
import model.structure.StructureType;

public class Structure extends EventEmitterActor {

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
	public StructureId id() {
		return new StructureId(id);
	}

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.structures().add(this);
		List<Structure> list = state.chunkToStructures().get(worldPos.chunkPos());
		if (list == null) {
			list = new ArrayList<>();
			state.chunkToStructures().put(worldPos.chunkPos(), list);
		}
		list.add(this);
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
