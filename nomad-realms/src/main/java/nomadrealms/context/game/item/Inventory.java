package nomadrealms.context.game.item;

import java.util.Collection;
import java.util.HashSet;

import nomadrealms.context.game.actor.types.HasInventory;

public class Inventory {

	private transient HasInventory owner;
	private boolean open = false;
	private Collection<WorldItem> items = new HashSet<>();

	/**
	 * No-arg constructor for serialization.
	 */
	protected Inventory() {
	}

	public Inventory(HasInventory owner) {
		this.owner = owner;
	}

	public void add(WorldItem item) {
		items.add(item);
		item.owner(owner);
		item.tile(null);
	}

	public void remove(WorldItem item) {
		items.remove(item);
		item.owner(null);
	}

	public Collection<WorldItem> items() {
		return items;
	}

	public boolean isOpen() {
		return open;
	}

	public void toggle() {
		open = !open;
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(HasInventory owner) {
		this.owner = owner;
		for (WorldItem item : items) {
			item.reindex(owner);
		}
	}

}
