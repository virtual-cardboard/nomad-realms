package nomadrealms.game.card.intent;

import nomadrealms.game.actor.HasInventory;
import nomadrealms.game.item.WorldItem;
import nomadrealms.game.world.World;

public class DropItemIntent implements Intent{

    private final HasInventory owner;
    private final WorldItem item;

    public DropItemIntent(HasInventory owner, WorldItem item) {
        this.owner = owner;
        this.item = item;
    }

    @Override
    public void resolve(World world) {
        owner.inventory().remove(item);
    }

}
