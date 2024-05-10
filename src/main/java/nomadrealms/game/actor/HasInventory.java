package nomadrealms.game.actor;

import nomadrealms.game.item.Inventory;
import nomadrealms.game.item.WorldItem;

public interface HasInventory extends HasPosition {

    public Inventory inventory();

    public default void addItem(WorldItem item) {
        inventory().add(item);
    }

    public default void removeItem(WorldItem item) {
        inventory().remove(item);
    }

}
