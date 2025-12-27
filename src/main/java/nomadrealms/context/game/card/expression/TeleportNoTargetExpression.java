package nomadrealms.context.game.card.expression;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.intent.Intent;
import nomadrealms.context.game.card.intent.TeleportIntent;
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

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		List<Tile> tiles = tile.find(world, source);
		if (tiles.isEmpty()) {
			return emptyList();
		}
		if (tiles.size() > 1) {
			throw new IllegalStateException("TeleportNoTargetExpression found multiple tiles");
		}
		return singletonList(new TeleportIntent(source, tiles.get(0), delay));
	}

}
