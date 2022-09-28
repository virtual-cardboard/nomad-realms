package model.actor;

import graphics.displayer.ItemDisplayer;
import model.id.ItemId;
import model.item.Item;
import model.state.GameState;

public class ItemActor extends Actor {

	private final Item item;

	public ItemActor(Item item) {
		this.item = item;
		setDisplayer(new ItemDisplayer());
	}

	@Override
	public ItemId id() {
		return new ItemId(id);
	}

	@Override
	public void update(long tick, GameState state) {
	}

	@Override
	public ItemActor copy() {
		ItemActor copy = new ItemActor(item);
		copy.setId(id);
		copy.setDisplayer(displayer());
		return super.copyTo(copy);
	}

	@Override
	public String description() {
		return item.toString();
	}

	public Item item() {
		return item;
	}

}
