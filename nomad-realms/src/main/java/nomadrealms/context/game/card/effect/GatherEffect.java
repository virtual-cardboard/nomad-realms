package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class GatherEffect extends Effect {


	private final Tile tile;
	private final int range;

	public GatherEffect(Actor source, Tile tile, int range) {
		super(source);
		this.tile = tile;
		this.range = range;
	}

	@Override
	public void resolve(World world) {
		System.out.println("Ranged harvest not yet implemented");
		while (!tile.items().isEmpty()) {
			WorldItem item = tile.items().get(0);
			tile.removeItem(item);
			source().inventory().add(item);
		}
	}

}
