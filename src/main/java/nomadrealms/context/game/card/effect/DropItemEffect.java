package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.HasInventory;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;

public class DropItemEffect extends Effect {

	private final HasInventory owner;
	private final WorldItem item;

	public DropItemEffect(HasInventory owner, WorldItem item) {
		this.owner = owner;
		this.item = item;
	}

	@Override
	public void resolve(World world) {
		owner.inventory().remove(item);
		owner.tile().addItem(item);
	}

}
