package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.HasInventory;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

import nomadrealms.context.game.actor.Actor;

public class DropItemEffect extends Effect {

	private final WorldItem item;
	private final Tile tile;

	public DropItemEffect(Actor source, WorldItem item, Tile tile) {
		this.source = source;
		this.item = item;
		this.tile = tile;
	}

	@Override
	public void resolve(World world) {
		source.inventory().remove(item);
		tile.addItem(item);
	}

}
