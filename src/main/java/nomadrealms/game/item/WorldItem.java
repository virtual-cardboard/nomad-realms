package nomadrealms.game.item;

import nomadrealms.game.actor.Actor;

public class WorldItem {

    Item item;
    Actor owner;

    public WorldItem(Item item) {
        this.item = item;
    }

    public void owner(Actor owner) {
        this.owner = owner;
    }
}
