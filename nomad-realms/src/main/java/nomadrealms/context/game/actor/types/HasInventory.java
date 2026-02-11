package nomadrealms.context.game.actor.types;

import engine.serialization.Derializable;
import nomadrealms.context.game.item.Inventory;
import nomadrealms.context.game.item.WorldItem;

@Derializable
public interface HasInventory {

	public Inventory inventory();

	public default void addItem(WorldItem item) {
		inventory().add(item);
	}

	public default void removeItem(WorldItem item) {
		inventory().remove(item);
	}

}
