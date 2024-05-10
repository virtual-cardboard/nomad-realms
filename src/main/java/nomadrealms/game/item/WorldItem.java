package nomadrealms.game.item;

import nomadrealms.game.actor.HasInventory;

public class WorldItem {

    Item item;
    HasInventory owner;

    public WorldItem(Item item) {
        this.item = item;
    }

    public HasInventory owner() {
        return owner;
    }

    public void owner(HasInventory owner) {
        this.owner = owner;
    }

}
