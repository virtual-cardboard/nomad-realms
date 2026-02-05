package nomadrealms.context.game.event;

import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import engine.serialization.Derializable;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.ui.custom.game.GameInterface;

@Derializable
public class DropItemEvent implements InputEvent {

	WorldItem worldItem;
	HasInventory source;
	private final Tile tile;

	/**
	 * No-arg constructor for serialization.
	 */
	protected DropItemEvent() {
		this.tile = null;
	}

	public DropItemEvent(WorldItem worldItem, HasInventory source, Tile tile) {
		this.worldItem = worldItem;
		this.source = source;
		this.tile = tile;
	}

	public WorldItem item() {
		return worldItem;
	}

	public HasInventory source() {
		return source;
	}

	public Tile tile() {
		return tile;
	}

	@Override
	public void resolve(World world) {
		world.resolve(this);
	}

	@Override
	public void resolve(GameInterface ui) {
		ui.resolve(this);
	}

	@Override
	public String toString() {
		return "DropItemEvent{" +
				"item=" + worldItem.item() +
				", source=" + source +
				'}';
	}
}
