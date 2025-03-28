package nomadrealms.game.item;

import nomadrealms.game.actor.HasInventory;
import nomadrealms.game.world.map.area.Tile;

public class WorldItem {

	final Item item;

	transient HasInventory owner;
	transient Tile tile;
	boolean buried = false;

	/**
	 * No-arg constructor for serialization.
	 */
	protected WorldItem() {
		item = null;
	}

	public WorldItem(Item item) {
		this.item = item;
	}

	public Item item() {
		return item;
	}

	public HasInventory owner() {
		return owner;
	}

	public void owner(HasInventory owner) {
		this.owner = owner;
	}

	public Tile tile() {
		return tile;
	}

	public void tile(Tile tile) {
		this.tile = tile;
	}

	public boolean buried() {
		return buried;
	}

	public void buried(boolean buried) {
		this.buried = buried;
	}

}
