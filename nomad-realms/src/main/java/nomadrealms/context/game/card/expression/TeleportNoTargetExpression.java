package nomadrealms.context.game.card.expression;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.effect.TeleportEffect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class TeleportNoTargetExpression implements CardExpression {

	private final Query<Tile> tile;
	int delay = 0;

	public TeleportNoTargetExpression(Query<Tile> tile, int delay) {
		this.tile = tile;
		this.delay = delay;
	}

	public static TeleportNoTargetExpression teleport(Query<Tile> tile, int delay) {
		return new TeleportNoTargetExpression(tile, delay);
	}

	@Override
	public List<Effect> effects(World world, Target target, CardPlayer source) {
		List<Tile> tiles = tile.find(world, source, target);
		if (tiles.isEmpty()) {
			return emptyList();
		}
		if (tiles.size() > 1) {
			throw new IllegalStateException("TeleportNoTargetExpression found multiple tiles");
		}
		return singletonList(new TeleportEffect(source, tiles.get(0), delay));
	}

}
