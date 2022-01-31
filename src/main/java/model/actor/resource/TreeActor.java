package model.actor.resource;

import static model.item.Item.WOOD;

import context.game.visuals.displayer.TreeActorDisplayer;
import model.actor.Actor;
import model.item.ItemCollection;
import model.state.GameState;

public class TreeActor extends Actor {

	private transient TreeActorDisplayer displayer;

	public TreeActor() {
		displayer = new TreeActorDisplayer(id);
	}

	private TreeActor(long id, TreeActorDisplayer displayer) {
		super(id);
		this.displayer = displayer;
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
	public void update(GameState state) {
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

}
