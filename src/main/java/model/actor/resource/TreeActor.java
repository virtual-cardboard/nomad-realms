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

	public TreeActor() {
		setDisplayer(new TreeActorDisplayer());
	}

	@Override
	public TreeActor copy() {
		TreeActor copy = new TreeActor();
		copy.setId(longID());
		copy.setDisplayer(displayer());
		return super.copyTo(copy);
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
		return "TreeActor ID=" + id + " " + worldPos();
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
