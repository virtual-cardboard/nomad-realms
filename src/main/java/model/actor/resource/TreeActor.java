package model.actor.resource;

import static model.actor.resource.ResourceActorSerializationFormats.TREE_ACTOR;
import static model.item.Item.WOOD;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import graphics.displayer.TreeActorDisplayer;
import model.id.TreeId;
import model.item.ItemCollection;
import model.state.GameState;

public class TreeActor extends ResourceActor {

	private transient TreeActorDisplayer displayer;

	public TreeActor() {
	}

	private TreeActor(long id, TreeActorDisplayer displayer) {
		super(id);
		this.displayer = displayer;
	}

	@Override
	public void setId(long id) {
		super.setId(id);
		displayer = new TreeActorDisplayer(id);
	}

	@Override
	public TreeActorDisplayer displayer() {
		return displayer;
	}

	@Override
	public TreeActor copy() {
		return super.copyTo(new TreeActor(id, displayer));
	}

	@Override
	public TreeId id() {
		return new TreeId(id);
	}

	@Override
	public void update(long tick, GameState state) {
	}

	@Override
	public String description() {
		return "TreeActor ID=" + id + " " + worldPos;
	}

	@Override
	public ItemCollection dropItems() {
		ItemCollection collection = new ItemCollection();
		collection.add(WOOD, 1);
		return collection;
	}

	@Override
	public ResourceActorSerializationFormats formatEnum() {
		return TREE_ACTOR;
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
	}

}
