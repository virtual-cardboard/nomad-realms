package nomadrealms.game.event;

import nomadrealms.game.actor.HasInventory;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;
import nomadrealms.render.ui.custom.game.GameInterface;

public class DropItemEvent implements InputEvent {

	WorldItem worldItem;
	HasInventory source;

	public DropItemEvent(WorldItem worldItem, HasInventory source) {
		this.worldItem = worldItem;
		this.source = source;
	}

	public WorldItem item() {
		return worldItem;
	}

	public HasInventory source() {
		return source;
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
