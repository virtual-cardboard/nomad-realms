package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class BuryItemEffect extends Effect {

	private final WorldItem item;
	private final Tile tile;

	public BuryItemEffect(Actor source, WorldItem item, Tile tile) {
		this.source = source;
		this.item = item;
		this.tile = tile;
	}

	@Override
	public void resolve(World world) {
		source.inventory().remove(item);
		tile.buryItem(item);
	}
}
