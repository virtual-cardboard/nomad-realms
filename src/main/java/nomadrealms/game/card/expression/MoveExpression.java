package nomadrealms.game.card.expression;

import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.MoveIntent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.Tile;

import java.util.List;

import static java.util.Collections.singletonList;

public class MoveExpression implements CardExpression {

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		if (isInRange(world, target, source, source.tile().range())) {
			return singletonList(new MoveIntent(source, (Tile) target));
		}
		return List.of();
	}

}
