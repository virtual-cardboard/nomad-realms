package nomadrealms.game.card.expression;

import nomadrealms.game.card.intent.Intent;
import nomadrealms.game.card.intent.MoveIntent;
import nomadrealms.game.event.Target;
import nomadrealms.game.actor.CardPlayer;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.tile.Tile;

import java.util.List;

import static java.util.Collections.singletonList;

public class MoveExpression implements CardExpression {

	@Override
	public List<Intent> intents(World world, Target target, CardPlayer source) {
		return singletonList(new MoveIntent(source, (Tile) target));
	}

}
