package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.item.Item;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class CutTreeEffect extends Effect {

	private final Structure target;

	public CutTreeEffect(Structure target) {
		this.target = target;
	}

	@Override
	public void resolve(World world) {
		Tile tile = target.tile();
		tile.clearActor();
		target.health(0);
		for (int i = 0; i < 3; i++) {
			tile.addItem(new WorldItem(Item.OAK_LOG));
		}
	}

}
