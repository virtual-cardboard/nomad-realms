package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.HasInventory;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class DropItemEffect extends Effect {

	private final HasInventory owner;
	private final WorldItem item;
	private final Tile tile;

	public DropItemEffect(HasInventory owner, WorldItem item, Tile tile) {
		this.owner = owner;
		this.item = item;
		this.tile = tile;
	}

	@Override
	public void resolve(World world) {
		owner.inventory().remove(item);
		tile.addItem(item);
	}

}
