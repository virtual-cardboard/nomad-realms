package model.actor;

import context.game.visuals.displayer.ItemDisplayer;
import model.item.Item;
import model.state.GameState;

public class ItemActor extends Actor {

	private transient ItemDisplayer displayer;
	private Item item;
	private boolean shouldRemove;

	public ItemActor(Item item) {
		this.item = item;
		this.displayer = new ItemDisplayer(id);
	}

	private ItemActor(long id, Item item, ItemDisplayer displayer) {
		super(id);
		this.item = item;
		this.displayer = displayer;
	}

	@Override
	public ItemDisplayer displayer() {
		return displayer;
	}

	@Override
	public void update(GameState state) {
	}

	@Override
	public ItemActor copy() {
		return super.copyTo(new ItemActor(id, item, displayer));
	}

	@Override
	public String description() {
		return item.toString();
	}

	public Item item() {
		return item;
	}

	@Override
	public boolean shouldRemove() {
		return shouldRemove;
	}

	public void setShouldRemove(boolean shouldRemove) {
		this.shouldRemove = shouldRemove;
	}

}
