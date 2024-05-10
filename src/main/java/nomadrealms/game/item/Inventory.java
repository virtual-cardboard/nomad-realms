package nomadrealms.game.item;

import nomadrealms.game.actor.HasInventory;

import java.util.Collection;
import java.util.HashSet;

public class Inventory {

    private HasInventory owner;
    private boolean open = false;
    private Collection<WorldItem> items = new HashSet<>();

    public Inventory(HasInventory owner) {
        this.owner = owner;
    }

    public void add(WorldItem item) {
        items.add(item);
        item.owner(owner);
    }

    public void remove(WorldItem item) {
        items.remove(item);
        item.owner(null);
    }

    public Iterable<WorldItem> items() {
        return items;
    }

    public boolean isOpen() {
        return open;
    }

    public void toggle() {
        open = !open;
    }

}
