package nomadrealms.game.item;

import java.util.Collection;
import java.util.HashSet;

public class Inventory {

    private boolean open = false;
    private Collection<WorldItem> items = new HashSet<>();

    public void add(WorldItem item) {
        items.add(item);
    }

    public void remove(WorldItem item) {
        items.remove(item);
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
