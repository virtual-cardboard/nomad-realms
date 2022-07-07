package model.actor;

import derealizer.format.SerializationFormatEnum;
import graphics.displayer.ItemDisplayer;
import model.id.ItemId;
import model.item.Item;
import model.state.GameState;

public class ItemActor extends Actor {

	private transient ItemDisplayer displayer;
	private Item item;

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
	public ItemId id() {
		return new ItemId(id);
	}

	@Override
	public ItemDisplayer displayer() {
		return displayer;
	}

	@Override
	public void update(long tick, GameState state) {
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
	public SerializationFormatEnum formatEnum() {
		return null;
	}

}
