package nomadrealms.context.game.item;

import engine.serialization.Derializable;
import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.world.map.area.Tile;

@Derializable
public class WorldItem {

	private Item item;

	private transient HasInventory owner;
	private transient Tile tile;
	private boolean buried = false;

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

    /**
     * Re-establishes the link to the owning inventory, typically after deserialization.
     */
	public void reindex(HasInventory owner) {
		this.owner = owner;
	}

	/**
	 * purely done for the sake of adding references to optimize other algorithms
	 */
	public void reindex(Tile tile) {
		this.tile = tile;
	}

}
